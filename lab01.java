import java.io.*;

class lab01 {
    public static void main(String[] args) throws IOException {
        String in = args[0];
        String out = args[1];
        String s;

        s = PassAnnotation.passAnnotation(in);
        // when the source is wrong,class will return null
        checkOut(s, "passAnootation");

        s = Token.getToken(s);
        checkOut(s, "getToken");

        s = Grammar.sys(s, out);
        checkOut(s, "Grammar");

    }

    private static void checkOut(String s, String loc) throws IOException {

        if (s == null) {
            throw new IOException("Wrong at " + loc);
        }
    }
}