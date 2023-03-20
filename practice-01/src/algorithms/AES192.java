package algorithms;

import utils.GeneratorsAES;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class AES192 {


    public static String encrypt(String clearText, SecretKey secretKey) throws
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException {

        //Montando o texto cifrado
        String cipherText = ImplAES.cipher(clearText, secretKey);

        return cipherText;
    }

    public static String decrypt(String cipherText, SecretKey secretKey) throws
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            BadPaddingException,
            InvalidKeyException {

        String clearText;

        clearText = ImplAES.decipher(cipherText, secretKey);

        return clearText;
    }

    abstract static class ImplAES {

        //Encriptando a mensagem
        public static String cipher(String clearText, SecretKey secretKey) throws
                NoSuchAlgorithmException,
                NoSuchPaddingException,
                InvalidKeyException,
                InvalidAlgorithmParameterException,
                IllegalBlockSizeException,
                BadPaddingException {

            byte[] cipherMessageBytes;
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, GeneratorsAES.getNewInitializationVector());
            cipherMessageBytes =
                    cipher.doFinal(clearText.getBytes());


            return encode(cipherMessageBytes);
        }

        private static String encode(byte[] cipherBytes) {
            String codedMessage =
                    Base64
                            .getEncoder()
                            .encodeToString(cipherBytes);

            return codedMessage;
        }


        private static byte[] decode(String cipherMessage) {
            byte[] cipherBytes =
                    Base64
                            .getDecoder()
                            .decode(cipherMessage);
            return cipherBytes;
        }

        //Decriptação da mensagem
        public static String decipher(String cipherText, SecretKey secretKey) throws
                NoSuchAlgorithmException,
                NoSuchPaddingException,
                InvalidKeyException,
                InvalidAlgorithmParameterException,
                IllegalBlockSizeException,
                BadPaddingException {

            byte[] cipherMessageBytes =
                    decode(cipherText);

            Cipher decipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            decipher.init(Cipher.DECRYPT_MODE, secretKey, GeneratorsAES.getGeneratedInitializationVector());

            byte[] decipherMessageBytes =
                    decipher.doFinal(cipherMessageBytes);

            return new String(decipherMessageBytes);
        }
    }
}