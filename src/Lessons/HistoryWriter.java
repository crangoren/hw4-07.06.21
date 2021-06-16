package Lessons;

import java.io.*;

public class HistoryWriter {


    public static File file = new File("chatHistory.db");


    public void saveHistory(String nick, String message) throws IOException {

        if (!file.exists()) {
            file.createNewFile();
        }
        try(FileWriter writer = new FileWriter(file, true))
        {
            writer.write(nick + " " + message + '\n');
            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    private int linesCount() throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean endsWithoutNewLine = false;
            while ((readChars = is.read(c)) != -1) {
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
                endsWithoutNewLine = (c[readChars - 1] != '\n');
            }
            if (endsWithoutNewLine) {
                ++count;
            }
            return --count;
        }
    }

    public StringBuilder getHistoryChat() throws IOException{
        if (!file.exists()){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
        String strLine;

        int linesNumber = linesCount();
        if(linesNumber > ChatConstants.VIEWCOUNT){
            for (int i = 0; i <= linesNumber - ChatConstants.VIEWCOUNT; i++) {
                br.readLine();
            }
        }




        while ((strLine = br.readLine()) != null){
            String[] list = strLine.split("-:-", 2);
            if(list.length == 2) {
                stringBuilder.append(list[0] + " " + list[1] + "\n");
            }
        }

        return stringBuilder;


    }


}
