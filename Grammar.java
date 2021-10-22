import java.io.*;
import java.util.ArrayList;

public class Grammar {

    private static String[] types;
    private static String[] tokens;

    private static int Number;
    private static int[] numbers;

    private static int ops;
    private static String[] op;

    private static ArrayList<Object>save;

    private static int numberTop = -1;
    private static int top = 0;
    private static boolean negative = false;


    private static Writer fileWriter;

    public static String sys(String in, String o) throws IOException {

        types = new String[100];
        tokens = new String[100];

        numbers = new int[100];
        op = new String[100];

        save = new ArrayList<>();

        File fp = new File(in);
        BufferedReader reader = new BufferedReader(new FileReader(fp));
        String line = new String();

        File out = new File(o);
        out.delete();
        out.createNewFile();

        while ((line = reader.readLine()) != null) {
            if (line.length() == 0)
                break;
            types[top] = line.split(" ")[0];
            tokens[top] = line.split(" ")[1];
            top++;
        }
        top = 0;
        reader.close();

        boolean flag = (isCompUnit() && tokens[top] == null);
        if (flag) {
            PrintInFile();
            fileWriter = new FileWriter(out.getPath(), true);
            fileWriter.write("define dso_local i32 @main(){\n");

            fileWriter.write("ret i32 " + numbers[numberTop] + "\n");
            fileWriter.write("}\n");

            System.out.println("define dso_local i32 @main(){");
            System.out.println("ret i32 " + numbers[numberTop] + "\n ");
            System.out.println("}");

            fileWriter.close();
            return out.getPath();
        } else {
            return null;
        }

    }

    private static boolean isCompUnit() {
        return isFuncDef();
    }

    private static boolean isFuncDef() {
        if (isFuncType() && isIdent()) {
            if (tokens[top].equals("("))
                top++;
            else
                return false;
            if (tokens[top].equals(")"))
                top++;
            else
                return false;
            return isBlock();
        }
        return false;
    }

    private static boolean isFuncType() {
        return tokenCheck("int");
    }

    private static boolean isIdent() {
        return tokenCheck("main");
    }

    private static boolean isBlock() {
        if (tokenCheck("{") && isStmt()) {
            return tokenCheck("}");
        }
        return false;
    }

    private static boolean isNumber() {
        if (types[top].equals("number")) {
            if (tokens[top].length() == 1) {
                Number = Integer.parseInt(tokens[top]);
            } else {
                if (tokens[top].charAt(0) == '0') {
                    if (tokens[top].charAt(1) == 'x' || tokens[top].charAt(1) == 'X') {
                        Number = Integer.parseInt(tokens[top].substring(2), 16);
                    } else {
                        Number = Integer.parseInt(tokens[top].substring(1), 8);
                    }
                } else
                    Number = Integer.parseInt(tokens[top]);
            }
            if(negative){
                Number = -Number;
            }
            negative = false;
            save.add(Number);
            numbers[++numberTop] = Number;
            top++;
            return true;
        }
        return false;
    }

    private static boolean isExp() {
        return isAddExp();
    }

    private static boolean isAddExp() {
        if(isMulExp()){
            return isAddExpPro();
        }
        return false;
    }

    private static boolean isAddExpPro(){
        int staTop = top;
        if(tokens[top].charAt(0) == '+' || tokens[top].charAt(0) == '-' ){
            top++;
            if(isMulExp()){
                save.add(tokens[staTop]);
               
                return  isAddExpPro();
            }
                
            else
                return false;
        }
        return true;
    }

    private static boolean isMulExp() {
        if(isUnaryExp()){
            return isMulExpPro();
        }
        
        return false;
    }

    private static boolean isMulExpPro(){
        int staTop = top;
        if(tokens[top].charAt(0) == '*' || tokens[top].charAt(0) == '/' || 
            tokens[top].charAt(0) == '%' ){
            top++;
            if(isUnaryExp() ){
                save.add(tokens[staTop]);
                return isMulExpPro();
            }
            else
                return false;
        }
        return true;
    }

    private static boolean isUnaryExp() {
        int staTop = top;
        if (isPrimaryExp()) {
            return true;
        }
        top = staTop;
        if (isUnaryOp()) {
            return isUnaryExp();
        }
        return false;
    }

    private static boolean isPrimaryExp() {
        int staTop = top;
        if (isNumber()) {
            return true;
        }
        top = staTop;
        if (tokenCheck("(") ) {
            Boolean oldnegative = negative;
            negative = false;
            if(isExp()){
                negative = oldnegative;
                return tokenCheck(")");
            }
            return false;
        }
        return false;
    }

    private static boolean isUnaryOp() {
        int staTop = top;
        if (tokenCheck("+"))
            return true;
        top = staTop;
        if (tokenCheck("-")){
            negative = !negative;
            return true;
        }
        top = staTop;
        return false;
    }

    private static boolean isStmt() {
        if (tokenCheck("return") && isExp()) {

            return tokenCheck(";");
        }
        return false;

    }

    private static boolean tokenCheck(String token) {
        if (tokens[top] != null && tokens[top].equals(token)) {
            top++;
            return true;
        }

        return false;
    }

    private static void PrintInFile(){
        numberTop = -1;
        for (Object i : save) {
            if(i instanceof Integer){
                System.out.println("Integer " + (Integer)i);
                numbers[++numberTop] = (Integer)i;
            }
            else if(i instanceof String){
                System.out.println("Op " + (String)i);
                numberTop--;
                if(i.equals("+")){
                    numbers[numberTop] = numbers[numberTop] + numbers[numberTop+1];
                }
                else if(i.equals("-")){
                    numbers[numberTop] = numbers[numberTop] - numbers[numberTop+1];

                }else if(i.equals("*")){
                    numbers[numberTop] = numbers[numberTop] * numbers[numberTop+1];

                }else if(i.equals("/")){
                    numbers[numberTop] = numbers[numberTop] / numbers[numberTop+1];

                }else if(i.equals("%")){
                    numbers[numberTop] = numbers[numberTop] % numbers[numberTop+1];

                }

            }
            
        }
        
    }
}
