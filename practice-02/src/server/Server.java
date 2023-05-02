package server;

import utils.Data;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

    public static void run() {
        try {
            DigitalBankImpl digitalBank = new DigitalBankImpl();

            DigitalBank skeleton = (DigitalBank) UnicastRemoteObject.exportObject(digitalBank, 0);

            LocateRegistry.createRegistry(20001);

            Registry registry = LocateRegistry.getRegistry(InetAddress.getLocalHost().getHostAddress(), 20001);

            registry.bind("DigitalBank", skeleton);

            System.err.println("Server ready!");

            Data.generateInitialClients();
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
