package communications.tcp;

import communications.tcp.client.Client;
import communications.tcp.server.Server;

import java.util.HashMap;
import java.util.Map;

public class Process implements Runnable {
    private final int centralPort = 5001;
    Map<Integer, Process> processes = new HashMap<>();
    private final int id;
    private final int port;
    private Integer message;
    private final Communication communication = CommunicationType.getCommunication();
    private Process process;

    public Process(int id, int port) {
        this.id = id;
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public Integer getMessage() {
        return message;
    }

    public void setMessage(Integer message) {
        this.message = message;
    }

    public Communication getCommunication() {
        return communication;
    }

    public Map<Integer, Process> getProcesses() {
        return processes;
    }

    public Process getProcess() {
        return process;
    }

    public void sendMessage(Process process, Integer message) {
        this.process = process;
        this.message = message;
        this.getProcesses().remove(this.getId());
    }

    public int getCentralPort() {
        return centralPort;
    }

    public void connections(Process P2, Process P3, Process P4) {
        this.processes.put(P2.getId(), P2);
        this.processes.put(P3.getId(), P3);
        this.processes.put(P4.getId(), P4);
    }

    @Override
    public void run() {

        if (this.getCentralPort() == this.getPort() && this.getCommunication().equals(Communication.UNICAST)) {
            Server server = new Server(this.getId(), this.getPort());
            Thread threadServer = new Thread(server);
            threadServer.start();
        } else if (this.getCentralPort() == this.getPort() && this.getCommunication().equals(Communication.BROADCAST)) {
            Server server = new Server(this.getId(), this.getPort(), this.getProcesses().size());
            Thread threadServer = new Thread(server);
            threadServer.start();
        } else if (this.getProcess() != null && this.getCommunication().equals(Communication.UNICAST)) {
            boolean condition = this.getId() < this.getProcess().getId();

            Client client = new Client(this.getId(), this.getCentralPort(), this.getMessage(), condition);
            Thread threadClient = new Thread(client);
            threadClient.start();

            Client receiptClient = new Client(this.getProcess().getId(), this.getCentralPort(), condition);
            Thread threadReceiptClient = new Thread(receiptClient);
            threadReceiptClient.start();
        } else if (this.getCommunication() != null && this.getCommunication().equals(Communication.BROADCAST)) {
            Client client = new Client(this.getId(), this.getCentralPort(), this.getMessage(), true);
            Thread threadClient = new Thread(client);
            threadClient.start();
        }
    }
}
