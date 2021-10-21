import java.io.*;

public class Grammar {

    private static String[] types;
    private static String[] tokens;
    private static int Number;
    private static int top = 0;
    private static boolean negative = false;
    private static Writer fileWriter;

    public static String sys(String in, String o) throws IOException {

        types = new String[100];
        tokens = new String[100];

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
            fileWriter = new FileWriter(out.getPath(), true);
            fileWriter.write("define dso_local i32 @main(){\n");

            fileWriter.write("ret i32 " + Number + "\n");
            fileWriter.write("}\n");

            System.out.println("define dso_local i32 @main(){");
            System.out.println("ret i32 " + Number + " ");
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
            top++;
            return true;
        }
        return false;
    }

    private static boolean isExp() {
        return isAddExp();
    }

    private static boolean isAddExp() {
        return isMulExp();
    }

    private static boolean isMulExp() {
        return isUnaryExp();
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
        if (tokenCheck("(") && isExp() && tokenCheck(")")) {
            return true;
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
}
