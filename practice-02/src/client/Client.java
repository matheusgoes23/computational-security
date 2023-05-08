package client;

import algorithms.RSA;
import client.view.ClientView;
import server.DigitalBank;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    static Scanner scanner;

    static String token = "-1";

    public static void run() {

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 20001);

            DigitalBank stubClient = (DigitalBank) registry.lookup("DigitalBank");

            while (true) {
                scanner = new Scanner(System.in);

                RSA.generateKeyPair(); // Gerando um novo par de chaves para esse cliente

                System.out.println(" --------------------------------------------");
                System.out.println("|  [0] - FAZER LOGIN                         |");
                System.out.println("|  [1] - CRIAR NOVA CONTA                    |");
                System.out.println(" --------------------------------------------");
                System.out.print("  DIGITE O NÚMERO DA AÇÃO QUE VOCÊ DESEJA: ");

                switch (scanner.nextInt()) {
                    case 0 -> {
                        token = login(stubClient);
                        ClientView clientView = new ClientView();
                        clientView.start(stubClient, token);
                    }
                    case 1 -> {
                        scanner = new Scanner(System.in);
                        ClientView clientView = new ClientView();
                        clientView.createAccount(stubClient);
                    }
                    default -> System.out.println("Comando inválido!");
                }

                token = "-1";
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static String login(DigitalBank stubClient) throws RemoteException, NoSuchAlgorithmException {
        while (token.equals("-1")) {
            scanner = new Scanner(System.in);
            System.out.print("Digite seu login: ");
            String login = scanner.nextLine();
            System.out.print("Digite sua senha: ");
            String password = scanner.nextLine();

            token = RSA.decrypt(stubClient.authenticate(RSA.encrypt(login), RSA.encrypt(password)));

            if (Objects.equals(token, "-1")) {
                System.err.println("Login ou senha inválidos! Tente novamente.");
                scanner.nextLine();
            }
        }

        return token;
    }
}
