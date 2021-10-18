import java.io.*;


class lab01 {
    public static void main(String[] args) throws IOException {
        File fp = new File(args[0]);
        String out = args[1];
        String s;

        s = PassAnnotation.passAnnotation(args[0]);

        if(s==null) {
            throw new IOException("Wrong!");
        }

        s = token.getToken(s);

        // System.out.println(s);
    
        File middle = new File(s);

        s  = Grammar.sys(middle,out);
        
        if(s==null) {
            throw new IOException("Wrong!");
        }

        System.out.println(s);
        
    }
}