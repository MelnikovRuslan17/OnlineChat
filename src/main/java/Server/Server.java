package Server;

import Logger.Logger;
import Settings.Settings;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {
    private static final ArrayList<User> connections = new ArrayList<>();
    private static final Logger logger = Logger.getInstance();
    private Settings settings;

    public Server() {
        this.settings = new Settings();
    }

    public void startServer() {
        System.out.println("Запуск сервера...");
        logger.writeLog("Запуск сервера...");
        try (ServerSocket serverSocket = new ServerSocket(settings.getPort())) {
            while (true) {
                try {
                    new User(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("Ошибка при подключении " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void onConnection(User user) {
        connections.add(user);
        sendToAllConnection("Новый участник чата " + user.getName());
        logger.writeLog("Новый участник чата " + user.getName());
    }

    public synchronized void onDisconnect(User user) {
        connections.remove(user);
        sendToAllConnection("Участник " + user.getName() + " покинул чат");
        logger.writeLog("Участник " + user.getName() + " покинул чат");

    }

    public synchronized void onReceiveString(User user, String inMessage) {
        sendToAllConnection(user.getName() + " " + inMessage);
        logger.writeLog(user.getName() + " " + inMessage);
    }

    public synchronized void clientsOnline(int clientsOnline) {
        sendToAllConnection("Участников в сети " + clientsOnline);
        logger.writeLog("Участников в сети " + clientsOnline);

    }

    public synchronized void sendToAllConnection(String inMessage) {
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) {
            connections.get(i).sendMessage(inMessage);
        }
    }

    public synchronized boolean checkName(String userName) {
        for (User name : connections) {
            if (name.getName().equals(userName)) return false;

        }
        return true;
    }
}


