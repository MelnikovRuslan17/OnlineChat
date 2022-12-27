package Settings;

import java.io.*;

public class Settings {
    private int port;
    private String host;

    public Settings() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("settings.txt"))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.startsWith("port: ")) {
                    port = Integer.parseInt(line.replaceAll("[^0-9]", ""));
                }
                if (line.startsWith("host: ")) {
                    host = line.split(" ")[1];

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
