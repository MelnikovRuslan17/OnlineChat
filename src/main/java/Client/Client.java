package Client;

import Settings.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Scanner scanner;
    private final Socket socket;
    private final BufferedReader bufferedReaderIn;
    private final PrintWriter printWriterOut;
    private Thread thread;
    private Thread thread1;

    public Client() {
        Settings settings = new Settings();
        try {
            socket = new Socket(settings.getHost(), settings.getPort());
            scanner = new Scanner(System.in);
            bufferedReaderIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriterOut = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startWork() {
        thread = new Thread(() -> {
            try {
                while (!thread.isInterrupted()) {
                    if (bufferedReaderIn.ready()) {
                        String inMessage = bufferedReaderIn.readLine();
                        System.out.println(inMessage);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

        thread1 = new Thread(() -> {
            while (!thread1.isInterrupted()) {
                if (scanner.hasNextLine()) {
                    String outMessage = scanner.nextLine();
                    printWriterOut.println(outMessage);
                    if (outMessage.equals("/exit")) {
                        disconnect();
                        break;
                    }
                }
            }
        });
        thread1.start();
    }

    public synchronized void disconnect() {
        thread.interrupt();
        thread1.interrupt();
        try {
            scanner.close();
            socket.close();
            bufferedReaderIn.close();
            printWriterOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
