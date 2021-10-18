import java.io.*;

public class PassAnnotation {

    private static FileWriter fileWriter;
    private static int inAnnotation;


    public static String passAnnotation(String in) throws IOException{
        File fp = new File(in);
        BufferedReader reader = new BufferedReader(new FileReader(fp));
        String line = new String();

        File out = new File("passAnnotationTemp.txt");
        out.delete();
        out.createNewFile();

        fileWriter = new FileWriter(out.getPath(), true);
        inAnnotation = 0;
        while ((line = reader.readLine()) != null) {
            getLine(line);
        }

        reader.close();
        fileWriter.close();
        if(inAnnotation == 0)
            return out.getPath();
        else
            return null;


    }

    private static void getLine(String line) throws IOException{
        
        for(int i=0;i<line.length();i++){
            char c = line.charAt(i);
            if(i != line.length()-1){
            if (c == '/' && line.charAt(i+1) == '/') {
                // space
                
                break;
            } else if (c == '/' && line.charAt(i+1) == '*') {
                inAnnotation = 1;
            }else if (c == '*' && line.charAt(i+1) == '/'){
                if(inAnnotation == 1){
                    i++;
                    inAnnotation = 0;
                }
            }else{
                if(inAnnotation == 0)
                    fileWriter.write(c);
            }
        }
        else{
                if(inAnnotation == 0)
                    fileWriter.write(c);
            }
        }
        fileWriter.write('\n');
    }
}
