package client.view;

import server.DigitalBank;
import utils.Account;
import utils.InvestmentType;

import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientView {
    static Scanner scanner = new Scanner(System.in);
    static String token;

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

        if (stubClient.createAccount(account, token) == 1) {
            System.out.println("Conta Criada!");
        } else {
            System.out.println("Erro ao Criar Conta!");
        }

        delay();
    }

    public void listMyAccount(DigitalBank stubClient) throws RemoteException {

        System.out.println("MEUS DADOS:");
        Account account = stubClient.findMyAccount(token);

        if (account != null) {
            System.out.println("Login: " + account.getLogin());
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

        amount = stubClient.withdraw(amount, token);

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

        amount = stubClient.deposit(amount, token);
        if (amount > 0) {
            System.out.println("Depósito Realizado!\nAgora você tem: " + amount);
        } else {
            System.out.println("Conta não encontrada!");
        }

        delay();
    }

    public void getBalance(DigitalBank stubClient) throws RemoteException {

        System.out.println("SALDO:");
        System.out.println("Seu saldo é: " + stubClient.balance(token));

        delay();
    }

    public void transfer(DigitalBank stubClient) throws RemoteException {

        System.out.println("TRANSFERÊNCIA:");
        System.out.print("Digite o número da conta que vai receber o valor: ");
        String number = scanner.nextLine();
        System.out.print("Digite o valor vai transferir: ");
        double valor = scanner.nextDouble();

        if (stubClient.transfer(number, valor, token) == 1) {
            System.out.println("Transferência Realizada!");
        } else {
            System.out.println("Erro na Transferência!");
        }

        delay();
    }

    public void investInSavings(DigitalBank stubClient) throws RemoteException {

        System.out.println("INVESTIMENTO NA POUPANÇA:");
        System.out.println(stubClient.investInSavings(token));

        delay();
    }

    public void investInFixedIncome(DigitalBank stubClient) throws RemoteException {

        System.out.println("INVESTIMENTO NA RENDA FIXA:");
        System.out.println(stubClient.investInFixedIncome(token));

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
