package Logger;

import javax.swing.text.DateFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private final PrintWriter printWriter;
    private static Logger INSTANCE = null;

    private Logger() {
        try {
            FileWriter fileWriter = new FileWriter("file.log", true);
            printWriter = new PrintWriter(fileWriter, true);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    public static Logger getInstance(){
        if(INSTANCE == null){
            synchronized (Logger.class){
                INSTANCE = new Logger();

            }
        }
        return INSTANCE;
    }
    public void writeLog(String log){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss dd-MM-yyyy");
        printWriter.println("[" + LocalDateTime.now().format(formatter) + "]: " + log);
    }
}
