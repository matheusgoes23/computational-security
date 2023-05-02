import client.Client;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server.run();
        Client.run();
    }
}
