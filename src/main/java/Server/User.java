package Server;

import java.io.*;
import java.net.Socket;

public class User {
    private final Socket socket;
    private final BufferedReader bufferedReaderIn;
    private final BufferedWriter bufferedWriterOut;
    private Thread rxThread;
    public static int clientsCount = 0;
    public String name;

    public User(Server server, Socket socket) throws IOException {
        clientsCount++;
        this.socket = socket;
        bufferedReaderIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriterOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        rxThread = new Thread(() -> {
            try {
                while (true) {
                    bufferedWriterOut.write("Добро пожаловать в чат если захотите выйти наберите /exit " + "\r\n");
                    bufferedWriterOut.flush();
                    bufferedWriterOut.write("Введите имя " + "\r\n");
                    bufferedWriterOut.flush();
                    name = bufferedReaderIn.readLine();
                    if (server.checkName(name)) {
                        break;
                    } else {
                        bufferedWriterOut.write("Такой пользователь уже есть " + "\r\n");
                        bufferedWriterOut.flush();
                    }
                }
                if (name != null) {
                    server.onConnection(User.this);
                    server.clientsOnline(clientsCount);
                } else {
                    disconnect();
                }
                while (!rxThread.isInterrupted()) {
                    String input = bufferedReaderIn.readLine();
                    if (input.equals("/exit") || input == null) {
                        disconnect();
                        break;
                    } else {
                        server.onReceiveString(User.this, input);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                server.onDisconnect(User.this);
                clientsCount--;
            }
        });
        rxThread.start();
    }

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Произошла ошибка " + e);
        }
    }

    public String getName() {
        return name;
    }

    public synchronized void sendMessage(String message) {
        try {
            bufferedWriterOut.write(message + "\r\n");
            bufferedWriterOut.flush();
        } catch (IOException e) {
            System.out.println("Участник покинул чат " + e);

        }
    }


}
