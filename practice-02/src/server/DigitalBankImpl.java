package server;

import algorithms.RSA;
import utils.Account;
import utils.Data;
import utils.InvestmentType;
import utils.Token;

import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;

public class DigitalBankImpl implements DigitalBank {

    @Override
    public String authenticate(String login, String password) throws RemoteException, NoSuchAlgorithmException {
        login = RSA.decrypt(login);
        password = RSA.decrypt(password);

        Account account = Data.getAccountByLogin(login);

        if (account != null && account.getPassword().equals(password)) {
            return RSA.encrypt(Token.generateToken(account));
        }

        return RSA.encrypt(String.valueOf(-1));
    }

    @Override
    public String createAccount(String encryptedAccount) {
        Account account = new Account(RSA.decrypt(encryptedAccount));

        List<Account> accounts = Data.getAllAccounts();
        accounts.add(account);
        Data.updateAccounts(accounts);
        return RSA.encrypt("1");
    }

    @Override
    public Account findAccount(String number) {
        for (Account account : Data.getAllAccounts()) {
            if (account.getNumber().equals(number)) return account;
        }
        return null;
    }

    @Override
    public String findMyAccount(String token) throws RemoteException {
        token = RSA.decrypt(token);

        for (Account account : Data.getAllAccounts()) {
            if (account.getNumber().equals(Token.getAccountNumber(token))) return RSA.encrypt(account.toString());
        }

        return RSA.encrypt("-1");
    }

    @Override
    public String withdraw(String encryptedAmount, String token) {
        double amount = Double.parseDouble(RSA.decrypt(encryptedAmount));
        token = RSA.decrypt(token);

        List<Account> accounts = Data.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getNumber().equals(Token.getAccountNumber(token))) {
                if (accounts.get(i).getAmount() < amount) return RSA.encrypt("0");
                accounts.get(i).setAmount(accounts.get(i).getAmount() - amount);
                Data.updateAccounts(accounts);
                return RSA.encrypt(String.valueOf(accounts.get(i).getAmount()));
            }
            return RSA.encrypt("0");
        }
        return RSA.encrypt("-1");
    }

    @Override
    public String deposit(String encryptedAmount, String token) {
        double amount = Double.parseDouble(RSA.decrypt(encryptedAmount));
        token = RSA.decrypt(token);

        List<Account> accounts = Data.getAllAccounts();
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getNumber().equals(Token.getAccountNumber(token))) {
                accounts.get(i).setAmount(accounts.get(i).getAmount() + amount);
                Data.updateAccounts(accounts);
                return RSA.encrypt(String.valueOf(accounts.get(i).getAmount()));
            }
        }
        return RSA.encrypt("0");
    }

    @Override
    public String balance(String token) {
        token = RSA.decrypt(token);

        double result = findAccount(Token.getAccountNumber(token)).getAmount();

        return RSA.encrypt(String.valueOf(result));
    }

    @Override
    public String transfer(String numberToReceive, String encryptedAmount, String token) {
        numberToReceive = RSA.decrypt(numberToReceive);
        double amount = Double.parseDouble(RSA.decrypt(encryptedAmount));
        token = RSA.decrypt(token);

        List<Account> accounts = Data.getAllAccounts();
        Account accountToSend = findAccount(Token.getAccountNumber(token));
        Account accountReceive = findAccount(numberToReceive);

        if (accountToSend == null || accountReceive == null) return RSA.encrypt("0");

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
            return RSA.encrypt("1");
        } else {
            return RSA.encrypt("0");
        }
    }

    @Override
    public String investInSavings(String token, String encryptedMonths) throws RemoteException {
        token = RSA.decrypt(token);
        int months = Integer.parseInt(RSA.decrypt(encryptedMonths));

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

        String result = "Em " + months + " meses renderá: " + new DecimalFormat("###.00").format(calculateSavingsInterest(account.getAmount(), months));

        return RSA.encrypt(result);
    }

    @Override
    public String investInFixedIncome(String token, String encryptedMonths) throws RemoteException {
        token = RSA.decrypt(token);
        int months = Integer.parseInt(RSA.decrypt(encryptedMonths));

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

        String result = "Em " + months + " meses renderá: " + new DecimalFormat("###.00").format(calculateFixedIncomeInterest(account.getAmount(), months));

        return RSA.encrypt(result);
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
