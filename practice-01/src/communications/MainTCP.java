package communications;

import algorithms.*;
import utils.GeneratorsAES;
import utils.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainTCP {
    static Scanner in = new Scanner(System.in);

    public static void applyCommunication(Algorithm algorithm, Communication communication) throws
            InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException {

        CommunicationType.setCommunication(communication);

        Process P1 = new Process(1, 5001);
        Process P2 = new Process(2, 5002);
        Process P3 = new Process(3, 5003);
        Process P4 = new Process(4, 5004);

        P1.connections(P2, P3, P4);

        System.out.println("Escolha qual processo deve enviar a mensagem:" + "\nP2: Digite 2" + "\nP3: Digite 3" + "\nP4: Digite 4");

        int sendingProcess = in.nextInt();

        boolean condition = true;
        while (condition) {
            switch (sendingProcess) {
                case 2 -> {
                    if (communication.equals(Communication.UNICAST)) {
                        P2.sendMessage(selectReceived(P3, P4), receiveMessage(algorithm));
                    } else {
                        P2.setMessage(receiveMessage(algorithm));
                    }
                    condition = false;
                }
                case 3 -> {
                    if (communication.equals(Communication.UNICAST)) {
                        P3.sendMessage(selectReceived(P2, P4), receiveMessage(algorithm));
                    } else {
                        P3.setMessage(receiveMessage(algorithm));
                    }
                    condition = false;
                }
                case 4 -> {
                    if (communication.equals(Communication.UNICAST)) {
                        P4.sendMessage(selectReceived(P2, P3), receiveMessage(algorithm));
                    } else {
                        P4.setMessage(receiveMessage(algorithm));
                    }
                    condition = false;
                }
                default -> {
                    System.out.println("Não existe esse processo. Tente outra vez!");
                    sendingProcess = in.nextInt();
                }
            }
        }

        List<Thread> processes = new ArrayList<>();
        processes.add(new Thread(P1));
        processes.add(new Thread(P2));
        processes.add(new Thread(P3));
        processes.add(new Thread(P4));

        for (var process : processes) {
            process.start();
        }
    }

    public static Process selectReceived(Process first, Process second) {
        int P1 = first.getId();
        int P2 = second.getId();

        System.out.println("Escolha qual processo deve receber a mensagem:" + "\nP" + first.getId() + ": Digite " + first.getId() + "\nP" + second.getId() + ": Digite " + second.getId());

        int receiptProcess = in.nextInt();

        while (true) {
            if (receiptProcess == first.getId()) {
                return first;
            } else if (receiptProcess == second.getId()) {
                return second;
            } else {
                System.out.println("Não existe esse processo. Tente outra vez!");
                receiptProcess = in.nextInt();
            }
        }
    }

    // Faz a escolha do tipo de algoritmo de criptografia que deve usar no processo de comunicação
    public static String receiveMessage(Algorithm algorithm) throws
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException {

        System.out.print("Digite a mensagem a ser criptogradafa: ");

        in.nextLine();
        var message = in.nextLine();

        while (true) {
            switch (algorithm) {
                case VIGENERE -> {
                    AlgorithmType.setAlgorithm(Algorithm.VIGENERE);
                    return Vigenere.encrypt(message, Key.getKey());
                }
                case VERNAM -> {
                    AlgorithmType.setAlgorithm(Algorithm.VERNAM);
                    return Vernam.encrypt(message, Key.getKey());
                }
                case AES_192 -> {
                    AlgorithmType.setAlgorithm(Algorithm.AES_192);
                    return AES192.encrypt(message, GeneratorsAES.getNewSecretKey(192));
                }
            }
        }
    }
}