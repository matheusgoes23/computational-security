package communications.tcp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final int id;
    private final int port;
    private String message;
    private final int amount;

    public Server(int id, int port) {
        this.id = id;
        this.port = port;
        this.amount = 1;
    }

    public Server(int id, int port, int amount) {
        this.id = id;
        this.port = port;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.getPort());
            Socket socket = serverSocket.accept();
            DataInputStream inflow = new DataInputStream(socket.getInputStream());

            this.setMessage(inflow.readLine());

            inflow.close();
            socket.close();
            serverSocket.close();

            for (int i = 0; i < this.getAmount(); i++) {
                ServerSocket serverSocket2 = new ServerSocket(this.getPort());
                Socket socket2 = serverSocket2.accept();

                DataOutputStream outflow = new DataOutputStream(socket2.getOutputStream());

                outflow.writeBytes(this.getMessage());

                outflow.close();
                socket2.close();
                serverSocket2.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
