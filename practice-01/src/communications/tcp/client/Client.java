package communications.tcp.client;

import algorithms.AES192;
import algorithms.AlgorithmType;
import algorithms.Vernam;
import algorithms.Vigenere;
import utils.GeneratorsAES;
import utils.Key;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private final int id;
    private final int centralPort;
    private String message;
    private final boolean condition;

    public Client(int id, int centralPort, String message, boolean condition) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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
                    Thread.sleep((1000L * this.getId()) + 1000L);
                }
            } else {
                Thread.sleep(4000L / this.getId());
            }

            Socket socket = new Socket("localhost", this.getCentralPort());

            if (this.getMessage() != null) {
                DataOutputStream outflow = new DataOutputStream(socket.getOutputStream());

                System.out.println("P" + this.getId() + " Enviou encriptada: [ " + this.getMessage() + " ]");
                outflow.writeBytes(this.getMessage());

                outflow.close();
            } else {
                DataInputStream inflow = new DataInputStream(socket.getInputStream());
                this.setMessage(inflow.readLine());
                System.out.println("P" + this.getId() + " Recebeu encriptada: [ " + this.getMessage() + " ]");

                switch (AlgorithmType.getAlgorithm()) {
                    case VIGENERE -> {
                        System.out.println("P" + this.getId() + " Gerou mensagem descriptada: [ " + Vigenere.decrypt(this.getMessage(), Key.getKey()) + " ]");
                    }
                    case VERNAM -> {
                        System.out.println("P" + this.getId() + " Gerou mensagem descriptada: [ " + Vernam.decrypt(this.getMessage(), Key.getKey()) + " ]");
                    }
                    case AES_192 -> {
                        System.out.println("P" + this.getId() + " Gerou mensagem descriptada: [ " + AES192.decrypt(this.getMessage(), GeneratorsAES.getGeneratedSecretKey()) + " ]");
                    }
                }

                inflow.close();
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
