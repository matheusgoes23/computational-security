package server;

import utils.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface DigitalBank extends Remote {
    String authenticate(String login, String password) throws RemoteException, NoSuchAlgorithmException;

    int createAccount(Account account, String token) throws RemoteException;

    Account findAccount(String number) throws RemoteException;

    Account findMyAccount(String token) throws RemoteException;

    double withdraw(double amount, String token) throws RemoteException;

    double deposit(double amount, String token) throws RemoteException;

    double balance(String token) throws RemoteException;

    int transfer(String numberToReceive, double amount, String token) throws RemoteException;

    String investInSavings(String token) throws RemoteException;

    String investInFixedIncome(String token) throws RemoteException;
}