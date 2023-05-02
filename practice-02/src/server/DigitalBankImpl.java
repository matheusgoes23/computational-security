package server;

import utils.*;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DigitalBankImpl implements DigitalBank {

    @Override
    public String authenticate(String login, String password) throws RemoteException, NoSuchAlgorithmException {

        Account account = Data.getAccountByLogin(login);

        if (account != null && account.getPassword().equals(password)) {
            return Token.generateToken(account);
        }

        return null;
    }

    @Override
    public int createAccount(Account account, String token) {
        List<Account> accounts = Data.getAllAccounts();
        accounts.add(account);
        Data.updateAccounts(accounts);
        return 1;
    }

    @Override
    public Account findAccount(String number) {
        for (Account account : Data.getAllAccounts()) {
            if (account.getNumber().equals(number)) return account;
        }
        return null;
    }

    @Override
    public Account findMyAccount(String token) throws RemoteException {
        for (Account account : Data.getAllAccounts()) {
            if (account.getNumber().equals(Token.getAccountNumber(token))) return account;
        }
        return null;
    }

    @Override
    public double withdraw(double amount, String token) {
        List<Account> accounts = Data.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getNumber().equals(Token.getAccountNumber(token))) {
                if (accounts.get(i).getAmount() < amount) return 0;
                accounts.get(i).setAmount(accounts.get(i).getAmount() - amount);
                Data.updateAccounts(accounts);
                return accounts.get(i).getAmount();
            }
            return 0;
        }
        return -1;
    }

    @Override
    public double deposit(double amount, String token) {
        List<Account> accounts = Data.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getNumber().equals(Token.getAccountNumber(token))) {
                accounts.get(i).setAmount(accounts.get(i).getAmount() + amount);
                Data.updateAccounts(accounts);
                return accounts.get(i).getAmount();
            }
        }
        return 0;
    }

    @Override
    public double balance(String token) {
        return findAccount(Token.getAccountNumber(token)).getAmount();
    }

    @Override
    public int transfer(String numberToReceive, double amount, String token) {
        List<Account> accounts = Data.getAllAccounts();
        Account accountToSend = findAccount(Token.getAccountNumber(token));
        Account accountReceive = findAccount(numberToReceive);

        if (accountToSend == null || accountReceive == null) return 0;

        accountToSend.setAmount(accountToSend.getAmount() - amount);
        accountReceive.setAmount(accountReceive.getAmount() + amount);

        int success = 0;
        for (Account account : accounts) {
            if (account.getNumber().equals(accountToSend.getNumber())) {
                account.setAmount(accountToSend.getAmount());
                success++;
            }
            if (account.getNumber().equals(accountReceive.getNumber())) {
                account.setAmount(accountReceive.getAmount());
                success++;
            }
        }
        if (success == 2) {
            Data.updateAccounts(accounts);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String investInSavings(String token) throws RemoteException {
        Account account = findAccount(Token.getAccountNumber(token));

        if (account.getInvestmentType().equals(InvestmentType.FIXED_INCOME)) {
            List<Account> accounts = Data.getAllAccounts();
            for (Account accountToInvest : accounts) {
                if (accountToInvest.getNumber().equals(account.getNumber())) {
                    accountToInvest.setInvestmentType(InvestmentType.SAVINGS);
                }
            }
            Data.updateAccounts(accounts);
        }

        return "Valor aplicado: " + account.getAmount() + "\nEm 3 meses renderá: " + new DecimalFormat("###.00").format(calculateSavingsInterest(account.getAmount(), 3)) + "\nEm 6 meses renderá: " + new DecimalFormat("###.00").format(calculateSavingsInterest(account.getAmount(), 6)) + "\nEm 12 meses renderá: " + new DecimalFormat("###.00").format(calculateSavingsInterest(account.getAmount(), 12));
    }

    @Override
    public String investInFixedIncome(String token) throws RemoteException {
        Account account = findAccount(Token.getAccountNumber(token));

        if (account.getInvestmentType().equals(InvestmentType.SAVINGS)) {
            List<Account> accounts = Data.getAllAccounts();
            for (Account accountToInvest : accounts) {
                if (accountToInvest.getNumber().equals(account.getNumber())) {
                    accountToInvest.setInvestmentType(InvestmentType.FIXED_INCOME);
                }
            }
            Data.updateAccounts(accounts);
        }

        return "Valor aplicado: " + account.getAmount() + "\nEm 3 meses renderá: " + new DecimalFormat("###.00").format(calculateFixedIncomeInterest(account.getAmount(), 3)) + "\nEm 6 meses renderá: " + new DecimalFormat("###.00").format(calculateFixedIncomeInterest(account.getAmount(), 6)) + "\nEm 12 meses renderá: " + new DecimalFormat("###.00").format(calculateFixedIncomeInterest(account.getAmount(), 12));
    }

    static double calculateSavingsInterest(double amount, int mounth) {
        for (int i = 0; i < mounth; i++) {
            amount = amount + (amount * 0.005);
        }
        return amount;
    }

    static double calculateFixedIncomeInterest(double amount, int mounth) {
        for (int i = 0; i < mounth; i++) {
            amount = amount + (amount * 0.015);
        }
        return amount;
    }
}
