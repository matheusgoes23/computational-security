package algorithms;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public abstract class RSA {

    // Tamanho da chave
    private static final int KEY_SIZE = 2048;
    // Definimos o expoente público como uma constante
    private static final BigInteger PUBLIC_EXPONENT = BigInteger.valueOf(65537);

    // Chave privada, pública e módulo
    private static BigInteger privateKey;
    private static BigInteger publicKey;
    private static BigInteger modulus;

    // Método para gerar o par de chaves pública e privada
    public static void generateKeyPair() {
        Random random = new Random();
        BigInteger p = generateLargePrime(KEY_SIZE / 2, random); // Gerando o primeiro número primo
        BigInteger q = generateLargePrime(KEY_SIZE / 2, random); // Gerando o segundo número primo
        modulus = p.multiply(q); // Calculando o módulo
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); // Calculando a função totiente de Euler
        publicKey = PUBLIC_EXPONENT; // Definindo a chave pública como o expoente público
        privateKey = publicKey.modInverse(phi); // Calculando a chave privada a partir da chave pública e da função totiente de Euler
    }

    // Método para criptografar uma mensagem
    public static String encrypt(String message) {
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8); // transforma a mensagem em um array de bytes usando o charset UTF-8
        BigInteger m = new BigInteger(1, messageBytes); // converte o array de bytes em um número inteiro positivo
        BigInteger c = m.modPow(publicKey, modulus); // criptografa o número inteiro positivo usando a chave pública e o módulo
        byte[] ciphertextBytes = c.toByteArray(); // transforma o número inteiro criptografado em um array de bytes
        return new String(ciphertextBytes, StandardCharsets.ISO_8859_1); // retorna o array de bytes como uma string usando o charset ISO-8859-1
    }

    // Método para descriptografar uma mensagem criptografada
    public static String decrypt(String cipherText) {
        byte[] ciphertextBytes = cipherText.getBytes(StandardCharsets.ISO_8859_1); // transforma a mensagem criptografada em um array de bytes usando o charset ISO-8859-1
        BigInteger c = new BigInteger(1, ciphertextBytes); // converte o array de bytes em um número inteiro positivo
        BigInteger m = c.modPow(privateKey, modulus); // descriptografa o número inteiro positivo usando a chave privada e o módulo
        byte[] messageBytes = m.toByteArray(); // transforma o número inteiro descriptografado em um array de bytes
        return new String(messageBytes, StandardCharsets.UTF_8); // retorna o array de bytes como uma string usando o charset UTF-8
    }

    // Método para gerar um número primo grande
    private static BigInteger generateLargePrime(int bitSize, Random random) {
        return BigInteger.probablePrime(bitSize, random); // Gerando um número primo aleatório com o tamanho especificado
    }
}
