package utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public abstract class GeneratorsAES {
    private static SecretKey secretKey;

    private static IvParameterSpec initializationVector;

    public static SecretKey getGeneratedSecretKey() {
        return secretKey;
    }

    public static IvParameterSpec getGeneratedInitializationVector() {
        return initializationVector;
    }

    public static SecretKey getNewSecretKey(int keySize) throws NoSuchAlgorithmException {
        return generateSecretKey(keySize);
    }

    public static IvParameterSpec getNewInitializationVector() {
        return generateInitializationVector();
    }

    public static void setSecretKey(SecretKey secretKey) {
        GeneratorsAES.secretKey = secretKey;
    }

    public static void setInitializationVector(IvParameterSpec initializationVector) {
        GeneratorsAES.initializationVector = initializationVector;
    }

    public static SecretKey generateSecretKey(int keySize) throws NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(192);
        secretKey = keyGenerator.generateKey();

        setSecretKey(secretKey);

        return secretKey;
    }

    public static IvParameterSpec generateInitializationVector() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        setInitializationVector(ivParameterSpec);

        return ivParameterSpec;
    }
}