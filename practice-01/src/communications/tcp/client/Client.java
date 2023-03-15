package communications.tcp.client;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;

public class Client implements Runnable {
    private final int id;
    private final int centralPort;
    private Integer message;
    private final boolean condition;

    public Client(int id, int centralPort, Integer message, boolean condition) {
        this.id = id;
        this.centralPort = centralPort;
        this.message = message;
        this.condition = condition;
    }

    public Client(int id, int centralPort, boolean condition) {
        this.id = id;
        this.centralPort = centralPort;
        this.condition = condition;
    }

    public int getId() {
        return id;
    }

    public int getCentralPort() {
        return centralPort;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public boolean getCondition() {
        return condition;
    }

    @Override
    public void run() {
        try {
            if (this.getCondition()) {
                if (this.getMessage() != null) {
                    Thread.sleep(1000);
                } else {
                    Thread.sleep(1000L * this.getId());
                }
            } else {
                Thread.sleep(4000L / this.getId());
            }

            Socket socket = new Socket("localhost", this.getCentralPort());

            if (this.getMessage() != null) {
                DataOutputStream outflow = new DataOutputStream(socket.getOutputStream());

                System.out.println("P" + this.getId() + " Enviou: [ " + this.getMessage() + " ]");
                outflow.writeInt(this.getMessage());

                outflow.close();
            } else {
                DataInputStream inflow = new DataInputStream(socket.getInputStream());
                this.setMessage(inflow.readInt());
                System.out.println("P" + this.getId() + " Recebeu: [ " + this.getMessage() + " ]");

                inflow.close();
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
