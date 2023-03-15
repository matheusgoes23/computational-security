import algorithms.Algorithm;
import communications.tcp.Communication;
import communications.tcp.MainTCP;

import java.util.Scanner;

public class Main {
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Escolha qual tipo de algoritmo de criptografia quer usar:" + "\nCifra de Vigenère: Digite 1" + "\nCifra de Vernam: Digite 2" + "\nAES 192: Digite 3");

        var algorithmType = in.nextLine();

        boolean condition = true;
        while (condition) {
            switch (algorithmType) {
                case "1" -> {
                    //Faz chamada para o método que vai receber o tipo de algoritmo de vigenère
                    // e escolher se a comunicação vai ser broadcast ou unicast
                    selectCommunicationType(Algorithm.VIGENERE);
                    condition = false;
                }
                case "2" -> {
                    //Faz chamada para o método que vai receber o tipo de algoritmo de vernam
                    // e escolher se a comunicação vai ser broadcast ou unicast
                    selectCommunicationType(Algorithm.VERNAM);
                    condition = false;
                }
                case "3" -> {
                    //Faz chamada para o método que vai receber o tipo de algoritmo AES-192
                    // e escolher se a comunicação vai ser broadcast ou unicast
                    selectCommunicationType(Algorithm.AES_192);
                    condition = false;
                }
                default -> {
                    System.out.println("Não existe esse algoritmo. Tente outra vez!");
                    algorithmType = in.nextLine();
                }
            }
        }
    }

    private static void selectCommunicationType(Algorithm algorithm) {
        System.out.println("Escolha qual tipo de comunicação quer usar:" + "\nBroadcast: Digite 1" + "\nUnicast: Digite 2");

        var communicationType = in.nextLine();

        boolean condition = true;
        while (condition) {
            switch (communicationType) {
                case "1" -> {
                    //Faz chamada para o método que vai receber o tipo de algoritmo já escolhido
                    // e o tipo de comunicação Broadcast
                    MainTCP.applyCommunication(algorithm, Communication.BROADCAST);
                    condition = false;
                }
                case "2" -> {
                    //Faz chamada para o método que vai receber o tipo de algoritmo já escolhido
                    // e o tipo de comunicação Unicast
                    MainTCP.applyCommunication(algorithm, Communication.UNICAST);
                    condition = false;
                }
                default -> {
                    System.out.println("Não existe esse tipo de comunicação. Tente outra vez!");
                    communicationType = in.nextLine();
                }
            }
        }
    }
}