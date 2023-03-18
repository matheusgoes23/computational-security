package communications;

import algorithms.Algorithm;
import algorithms.AlgorithmType;
import algorithms.Vigenere;
import utils.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainTCP {
    static Scanner in = new Scanner(System.in);

    public static void applyCommunication(Algorithm algorithm, Communication communication) {
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

    /* TODO devo criar classes que fazem a encriptação e decriptação de cada tipo de algoritmo de criptografia
        e trocar a saída desse metodo pelo texto cifrado, assim envia o texto cifrado para cada processo
     */
    public static String receiveMessage(Algorithm algorithm) {
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
                    //AlgorithmType.setAlgorithm(Algorithm.VERNAM);
                    //return Vernam.encrypt(message, Key.getKey());
                }
                case AES_192 -> {
                    //AlgorithmType.setAlgorithm(Algorithm.AES_192);
                    //return AES192.encrypt(message, Key.getKey());
                }
            }
        }
    }
}