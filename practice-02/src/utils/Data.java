package utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Data {
    private static List<Account> accounts = new ArrayList<>();

    public static List<Account> getAllAccounts() {
        return accounts;
    }

    public static void updateAccounts(List<Account> accounts) {
        Data.accounts = accounts;
    }

    public static Account getAccountByLogin(String login) {

        for (Account account : accounts) {
            if (account.getLogin().equals(login)) {
                return account;
            }
        }
        return null;
    }

    public static void generateInitialClients() {
        for (int i = 1; i < 4; i++) {
            Account account = new Account();
            account.setLogin(account.getNumber());
            account.setPassword("123456");
            account.setName("client0" + i);
            account.setAmount(100.0);
            accounts.add(account);

            System.out.println("Cliente inicial [ login: " + account.getLogin() + ", senha: " + account.getPassword() + " ]");
        }
    }
}
