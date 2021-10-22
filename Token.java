import java.io.*;

public class Token {
    private static Writer fileWriter;

    public static String getToken(String in) throws IOException {

        File fp = new File(in);
        BufferedReader reader = new BufferedReader(new FileReader(fp));
        String line = new String();

        File out = new File("getTokenTemp.txt");
        out.delete();
        out.createNewFile();

        fileWriter = new FileWriter(out.getPath(), true);
        while ((line = reader.readLine()) != null) {
            getLine(line);
        }

        reader.close();
        fileWriter.close();
        return out.getPath();
    }

    private static void getLine(String line) throws IOException {
        String temp = new String();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            temp = null;
            if (c == ' ' || c == '\t' || c == '\n') {
                // space
                continue;
            } else if (Character.isLetter(c)) {
                // identity
                int sta = i;
                while (i < line.length() && Character.isLetter(line.charAt(i))) {
                    i++;
                }
                temp = "Identify " + line.substring(sta, i);
                i--;

            } else if (Character.isDigit(c)) {
                // number
                int sta = i;
                if (c != '0') {
                    i++;
                    while (i < line.length() && Character.isDigit(line.charAt(i))) {
                        i++;
                    }
                    temp = "number " + line.substring(sta, i);
                    i--;
                } else {
                    if (i + 2 < line.length() && (line.charAt(i + 1) == 'x' || line.charAt(i + 1) == 'X')
                            && isHex(line.charAt(i + 2))) {
                        i++;
                        i++;
                        while (i < line.length() && isHex(line.charAt(i))) {
                            i++;
                        }

                        temp = "number " + line.substring(sta, i);
                        i--;

                    } else {
                        while (i < line.length() && isOctal(line.charAt(i))) {
                            i++;
                        }

                        temp = "number " + line.substring(sta, i);
                        i--;
                    }
                }

            } else {
                // other
                temp = "other " + Character.toString(c);
            }

            fileWriter.write(temp + '\n');
            System.out.println(temp);
        }
    }

    private static boolean isOctal(Character s) {
        return (s >= '0' && s <= '7');
    }

    private static boolean isHex(Character s) {
        return (Character.isDigit(s) || (s >= 'a' && s <= 'f') || (s >= 'A' && s <= 'F'));
    }
}
