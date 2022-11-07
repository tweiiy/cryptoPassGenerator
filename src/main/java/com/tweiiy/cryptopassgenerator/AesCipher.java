package com.tweiiy.cryptopassgenerator;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Hex;

public class AesCipher {
    private static final String INIT_VECTOR = "64jjIZWUZK7bwTz4"; // 128 bit вектор инициализации

    public static String encrypt (String secretKey, String plainText){
        try{
            if (!isKeyLengthValid(secretKey))
                throw new Exception("secret key's length must be 128, 192 of 256 bites");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            return new String(Hex.encodeHex(cipher.doFinal(plainText.getBytes("UTF-8")), false));
        } catch (Throwable cause) {
            System.out.println(cause.getMessage());
        }
        return null;
    }

    public static String decrypt(String secretKey, String cipherText){
        try {
            if (!isKeyLengthValid(secretKey))
                throw new Exception("secret key's length must be 128, 192 of 256 bites");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return new String(cipher.doFinal(Hex.decodeHex(cipherText.toCharArray())));
        } catch (Throwable cause){
            cause.printStackTrace();
        }
        return null;
    }

    private static boolean isKeyLengthValid(String key){
        return key.length() == 16 || key.length() == 24 || key.length() == 32;
    }
}
