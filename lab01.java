import java.io.*;


class lab01 {
    public static void main(String[] args) throws IOException {
        File fp = new File(args[0]);
        String out = args[1];
        String s;

       

        s = token.getToken(fp);

        // System.out.println(s);
    
        File middle = new File(s);

        s  = Grammar.sys(middle,out);
        
        if(s==null) {
            throw new IOException("Wrong!");
        }
        
    }
}