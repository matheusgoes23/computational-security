package algorithms;

public abstract class Vigenere {
    public static String encrypt(String clearText, String key) {

        //Completando a chave se ela for menor que a mensagem
        for (int i = 0; key.length() < clearText.length(); i++) {
            key += key.charAt(i);
        }

        //Montando o texto cifrado
        String cipherText = "";

        for (int i = 0; i < clearText.length(); i++) {
            cipherText += getEncryptedLetterFromVigenereTable(
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

        //Montando o texto descriptografado
        String clearTex = "";

        for (int i = 0; i < cipherText.length(); i++) {
            clearTex += getDecryptedLetterFromVigenereTable(
                    String.valueOf(cipherText.charAt(i)),
                    String.valueOf(key.charAt(i)));
        }

        return clearTex;
    }

    //Retona uma letra cifrada da tabela de vigenere
    private static String getEncryptedLetterFromVigenereTable(String messageLetter, String keyLetter) {

        //Obter a tabela de vigenere
        String[][] vigenereTable = generateVigenereTable();

        /* Procurando a letra na tabela de vigenere gerada pela combinação da
         * letra do texto claro e da letra da chave
         * */
        for (int i = 0; i < vigenereTable.length; i++) {
            for (int j = 0; j < vigenereTable.length; j++) {
                if (i == messageLetter.hashCode() - 97 && j == keyLetter.hashCode() - 97) {
                    return vigenereTable[i][j];
                }
            }
        }

        return null;
    }

    //Retona uma letra cifrada da tabela de vigenere
    private static String getDecryptedLetterFromVigenereTable(String messageLetter, String keyLetter) {

        //Obter a tabela de vigenere
        String[][] vigenereTable = generateVigenereTable();

        /* Procurando a letra na tabela de vigenere gerada pela combinação da
         * letra do texto cifrado e da letra da chave
         * */
        for (int i = 0; i < vigenereTable.length; i++) {
            if (i == messageLetter.hashCode() - 97) {
                for (int j = 0; j < vigenereTable.length; j++) {
                    if (String.valueOf((char) (i + 97)).equals(vigenereTable[keyLetter.hashCode() - 97][j])) {
                        return String.valueOf((char) (j + 97));
                    }
                }
            }
        }

        return null;
    }

    //Montando a tabela de vigenere
    private static String[][] generateVigenereTable() {
        String[][] vigenereTable = new String[26][26];
        boolean condition = true;

        for (int i = 0, x = 0; i < vigenereTable.length; i++, x = i) {
            condition = true;
            for (int j = 0; j < vigenereTable.length; j++, x++) {
                vigenereTable[i][j] = String.valueOf((char) (97 + x));

                if ((x + 1 == vigenereTable.length) && condition) {
                    x = -1;
                    condition = false;
                }
            }
        }

        return vigenereTable;
    }
}

