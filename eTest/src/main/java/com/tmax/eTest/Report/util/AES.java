package com.tmax.eTest.Report.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

// import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
 
//Source from https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
@Slf4j
public class AES {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            log.error("Invalid algorithm selected.");
        } 
        catch (UnsupportedEncodingException e) {
            log.error("Given data's encoding is not supported.");
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (NoSuchAlgorithmException e) { log.error("Invalid algorithm selected.");}
        catch (NoSuchPaddingException e) { log.error("Invalid padding option.");}
        catch (InvalidKeyException e) { log.error("Invalid key given.");}
        catch (IllegalStateException e) { log.error("Encoding failed due to illegal state");}
        catch (IllegalBlockSizeException e) { log.error("Encoding failed due to bad block size");}
        catch (BadPaddingException e) { log.error("Encoding failed due to bac padding");}
        catch (UnsupportedEncodingException e) { log.error("Encoding type not supported.");}

        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (NoSuchAlgorithmException e) { log.error("Invalid algorithm selected.");}
        catch (NoSuchPaddingException e) { log.error("Invalid padding option.");}
        catch (InvalidKeyException e) { log.error("Invalid key given.");}
        catch (IllegalStateException e) { log.error("Encoding failed due to illegal state");}
        catch (IllegalBlockSizeException e) { log.error("Encoding failed due to bad block size");}
        catch (BadPaddingException e) { log.error("Encoding failed due to bac padding");}

        return null;
    }
}