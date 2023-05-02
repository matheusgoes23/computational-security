package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public abstract class Token {
    private static final Map<String, String> tokenToNumberMap = new HashMap<String, String>();

    public static String generateToken(Account account) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // Gera o resumo criptográfico da string de entrada
        byte[] hash = md.digest(account.getNumber().getBytes());
        // Codifica o resumo criptográfico em uma string legível usando a classe Base64
        String token = Base64.getEncoder().encodeToString(hash);

        //Armazena a relação entre a conta e o token gerado
        tokenToNumberMap.put(token, account.getNumber());

        return token;
    }

    public static String getAccountNumber(String token) {
        return tokenToNumberMap.get(token);
    }
}
