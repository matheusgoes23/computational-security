package server;

import utils.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public interface DigitalBank extends Remote {
    String authenticate(String login, String password) throws RemoteException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException;

    String createAccount(String account) throws RemoteException;

    Account findAccount(String number) throws RemoteException;

    String findMyAccount(String token) throws RemoteException;

    String withdraw(String amount, String token) throws RemoteException;

    String deposit(String amount, String token) throws RemoteException;

    String balance(String token) throws RemoteException;

    String transfer(String numberToReceive, String amount, String token) throws RemoteException;

    String investInSavings(String token, String months) throws RemoteException;

    String investInFixedIncome(String token, String months) throws RemoteException;
}