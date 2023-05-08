package client.view;

import algorithms.RSA;
import server.DigitalBank;
import utils.Account;
import utils.InvestmentType;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientView {
    static Scanner scanner = new Scanner(System.in);
    static String token = "-1";

    public void start(DigitalBank stubClient, String receivedToken) throws RemoteException {
        token = receivedToken;

        while (true) {
            System.out.println(" --------------------------------------------");
            System.out.println("|  [0] - SAIR DESSA CONTA                    |");
            System.out.println("|  [1] - LISTAR SUA CONTA                    |");
            System.out.println("|  [2] - SACAR UM VALOR DA SUA CONTA         |");
            System.out.println("|  [3] - DEPOSITAR UM VALOR NA SUA CONTA     |");
            System.out.println("|  [4] - VER O SALDO DA SUA CONTA            |");
            System.out.println("|  [5] - TRANSFERÊNCIA DE VALOR              |");
            System.out.println("|  [6] - INVESTIMENTO NA POUPANÇA            |");
            System.out.println("|  [7] - INVESTIMENTO NA RENDA FIXA          |");
            System.out.println(" --------------------------------------------");
            System.out.print("  DIGITE O NÚMERO DA AÇÃO QUE VOCÊ DESEJA: ");

            switch (scanner.nextInt()) {
                case 0 -> {
                    scanner = new Scanner(System.in);
                    System.out.println();
                    return;
                }
                case 1 -> {
                    scanner = new Scanner(System.in);
                    listMyAccount(stubClient);
                }
                case 2 -> {
                    scanner = new Scanner(System.in);
                    withdraw(stubClient);
                }
                case 3 -> {
                    scanner = new Scanner(System.in);
                    deposit(stubClient);
                }
                case 4 -> {
                    scanner = new Scanner(System.in);
                    getBalance(stubClient);
                }
                case 5 -> {
                    scanner = new Scanner(System.in);
                    transfer(stubClient);
                }
                case 6 -> {
                    scanner = new Scanner(System.in);
                    investInSavings(stubClient);
                }
                case 7 -> {
                    scanner = new Scanner(System.in);
                    investInFixedIncome(stubClient);
                }
                default -> System.out.println("Comando inválido!");
            }
        }
    }

    public void createAccount(DigitalBank stubClient) throws RemoteException {
        Account account = new Account();

        System.out.println("CRIAÇÃO DE NOVA CONTA:");
        System.out.print("Seu login e número da sua conta: " + account.getNumber() + "\n");
        account.setLogin(account.getNumber());
        System.out.print("Digite uma senha: ");
        account.setPassword(scanner.nextLine());
        System.out.print("Digite um cpf: ");
        account.setCpf(scanner.nextLine());
        System.out.print("Digite o nome: ");
        account.setName(scanner.nextLine());
        System.out.print("Digite o endereço: ");
        account.setAddress(scanner.nextLine());
        System.out.print("Digite a data de nascimento: ");
        account.setBirth(scanner.nextLine());
        System.out.print("Digite o telefone: ");
        account.setPhone(scanner.nextLine());

        String encryptedResult = stubClient.createAccount(RSA.encrypt(account.toString()));

        String result = RSA.decrypt(encryptedResult);

        if (result.equals("1")) {
            System.out.println("Conta Criada!");
        } else {
            System.out.println("Erro ao Criar Conta!");
        }

        delay();
    }

    public void listMyAccount(DigitalBank stubClient) throws RemoteException {

        System.out.println("MEUS DADOS:");

        String encryptedResult = stubClient.findMyAccount(RSA.encrypt(token));
        String result = RSA.decrypt(encryptedResult);

        if (!result.equals("-1")) {

            Account account = new Account(result);
            System.out.println("Login: " + account.getNumber());
            System.out.println("Senha: " + account.getPassword());
            System.out.print("Tipo da conta: ");
            if (account.getInvestmentType().equals(InvestmentType.SAVINGS)) System.out.println("Poupança");
            else if (account.getInvestmentType().equals(InvestmentType.FIXED_INCOME))
                System.out.println("Renda Fixa");
            System.out.println("Número da conta: " + account.getNumber());
            System.out.println("Saldo da conta: " + account.getAmount());
            System.out.println("CPF: " + account.getCpf());
            System.out.println("Dono da conta: " + account.getName());
            System.out.println("Endereço : " + account.getAddress());
            System.out.println("Data de nascimento: " + account.getBirth());
            System.out.println("Telefone: " + account.getPhone());
            System.out.println();
        } else {
            System.out.println("Conta não encontrada!");
        }
        delay();
    }

    public void withdraw(DigitalBank stubClient) throws RemoteException {

        System.out.println("SAQUE:");
        System.out.print("Digite o valor vai sacar: ");
        double amount = scanner.nextDouble();

        String encryptedAmount = stubClient.withdraw(
                RSA.encrypt(String.valueOf(amount)),
                RSA.encrypt(token));
        amount = Double.parseDouble(RSA.decrypt(encryptedAmount));

        if (amount == -1) {
            System.out.println("Erro ao sacar!");
        } else {
            System.out.println("Saque Realizado!\nAgora você tem: " + amount);
        }

        delay();
    }

    public void deposit(DigitalBank stubClient) throws RemoteException {

        System.out.println("DEPOSITO:");
        System.out.print("Digite o valor vai depositar: ");
        double amount = scanner.nextDouble();

        String encryptedAmount = stubClient.deposit(RSA.encrypt(String.valueOf(amount)), RSA.encrypt(token));
        amount = Double.parseDouble(RSA.decrypt(encryptedAmount));

        if (amount > 0) {
            System.out.println("Depósito Realizado!\nAgora você tem: " + amount);
        } else {
            System.out.println("Conta não encontrada!");
        }

        delay();
    }

    public void getBalance(DigitalBank stubClient) throws RemoteException {

        String encryptedResult = stubClient.balance(RSA.encrypt(token));
        double result = Double.parseDouble(RSA.decrypt(encryptedResult));

        System.out.println("SALDO:");
        System.out.println("Seu saldo é: " + result);

        delay();
    }

    public void transfer(DigitalBank stubClient) throws RemoteException {

        System.out.println("TRANSFERÊNCIA:");
        System.out.print("Digite o número da conta que vai receber o valor: ");
        String number = scanner.nextLine();
        System.out.print("Digite o valor vai transferir: ");
        double valor = scanner.nextDouble();

        String encryptedResult = stubClient.transfer(
                RSA.encrypt(number),
                RSA.encrypt(String.valueOf(valor)),
                RSA.encrypt(token));
        int result = Integer.parseInt(RSA.decrypt(encryptedResult));

        if (result == 1) {
            System.out.println("Transferência Realizada!");
        } else {
            System.out.println("Erro na Transferência!");
        }

        delay();
    }

    public void investInSavings(DigitalBank stubClient) throws RemoteException {

        String resultInThreeMonths = RSA.decrypt(stubClient.investInSavings(RSA.encrypt(token), RSA.encrypt("3")));
        String resultInSixMonths = RSA.decrypt(stubClient.investInSavings(RSA.encrypt(token), RSA.encrypt("6")));
        String resultInTwelveMonths = RSA.decrypt(stubClient.investInSavings(RSA.encrypt(token), RSA.encrypt("12")));

        String encryptedResult = stubClient.findMyAccount(RSA.encrypt(token));
        String result = RSA.decrypt(encryptedResult);

        Account account = new Account(result);

        System.out.println("INVESTIMENTO NA POUPANÇA:");
        System.out.println("Valor aplicado: " + account.getAmount());
        System.out.println(resultInThreeMonths);
        System.out.println(resultInSixMonths);
        System.out.println(resultInTwelveMonths);

        delay();
    }

    public void investInFixedIncome(DigitalBank stubClient) throws RemoteException {

        String resultInThreeMonths = RSA.decrypt(stubClient.investInFixedIncome(RSA.encrypt(token), RSA.encrypt("3")));
        String resultInSixMonths = RSA.decrypt(stubClient.investInFixedIncome(RSA.encrypt(token), RSA.encrypt("6")));
        String resultInTwelveMonths = RSA.decrypt(stubClient.investInFixedIncome(RSA.encrypt(token), RSA.encrypt("12")));

        String encryptedResult = stubClient.findMyAccount(RSA.encrypt(token));
        String result = RSA.decrypt(encryptedResult);

        Account account = new Account(result);

        System.out.println("INVESTIMENTO NA RENDA FIXA:");
        System.out.println("Valor aplicado: " + account.getAmount());
        System.out.println(resultInThreeMonths);
        System.out.println(resultInSixMonths);
        System.out.println(resultInTwelveMonths);

        delay();
    }

    private void delay() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
