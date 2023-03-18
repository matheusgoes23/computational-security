package algorithms;

public abstract class Vernam {
    public static String encrypt(String clearText, String key) {

        //Completando a chave se ela for menor que a mensagem
        for (int i = 0; key.length() < clearText.length(); i++) {
            key += key.charAt(i);
        }

        //Montando o texto cifrado
        String cipherText = "";

        for (int i = 0; i < clearText.length(); i++) {
            cipherText += getXOROperationCipherLetter(
                    String.valueOf(clearText.charAt(i)),
                    String.valueOf(key.charAt(i)));
        }

        return cipherText;
    }

    public static String decrypt(String cipherText, String key) {
        //Completando a chave se ela for menor que a mensagem
        for (int i = 0; key.length() < cipherText.length(); i++) {
            key += key.charAt(i);
        }

        String letterInBinary = "";
        String cipherLetters = "";

        //Obtendo letras cifradas a partir do texto cifrado
        for (int i = 0, bitCount = 0; i < cipherText.length(); i++, bitCount++) {
            letterInBinary += String.valueOf(cipherText.charAt(i));

            if (bitCount == 8) {
                cipherLetters += convertBinaryToLetter(letterInBinary);
                bitCount = -1;
                letterInBinary = "";
            }
        }

        //Montando o texto decodificado
        String clearText = "";

        for (int i = 0; i < cipherLetters.length(); i++) {
            clearText += getXOROperationClearLetter(
                    String.valueOf(cipherLetters.charAt(i)),
                    String.valueOf(key.charAt(i)));
        }

        return clearText;
    }

    //Retona uma letra cifrada de uma operação XOR
    private static String getXOROperationCipherLetter(String messageLetter, String keyLetter) {
        String messageLetterInBinary = convertLetterToBinary(messageLetter);
        String keyLetterInBinary = convertLetterToBinary(keyLetter);

        String XOROperationLetter = "";

        //Fazendo operação XOR bit por bit
        for (int i = 0; i < messageLetterInBinary.length(); i++) {
            if (messageLetterInBinary.charAt(i) == keyLetterInBinary.charAt(i)) {
                XOROperationLetter += "0";
            } else {
                XOROperationLetter += "1";
            }
        }

        return XOROperationLetter;
    }

    //Retona uma letra descriptografada de uma operação XOR
    private static String getXOROperationClearLetter(String messageLetter, String keyLetter) {
        String messageLetterInBinary = convertLetterToBinary(messageLetter);
        String keyLetterInBinary = convertLetterToBinary(keyLetter);

        String XOROperationLetter = "";

        //Fazendo operação XOR bit por bit
        for (int i = 0; i < messageLetterInBinary.length(); i++) {
            if (messageLetterInBinary.charAt(i) == keyLetterInBinary.charAt(i)) {
                XOROperationLetter += "0";
            } else {
                XOROperationLetter += "1";
            }
        }

        //Transformando binario em letra
        XOROperationLetter = convertBinaryToLetter(XOROperationLetter);

        return XOROperationLetter;
    }

    //Transformar em letra em binário

    private static String convertLetterToBinary(String letter) {
        String binary = Integer.toBinaryString(letter.hashCode());

        for (int i = 0; binary.length() <= 8; i++) {
            String aux = binary;
            binary = "0";
            binary += aux;
        }

        return binary;
    }

    //Retona uma letra cifrada da tabela de vigenere
    private static String convertBinaryToLetter(String binary) {
        return String.valueOf((char) Integer.parseInt(binary, 2));
    }
}

