package com.cienciacomputacao.osqr.util;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

    public static String encrypt(String data) throws Exception {
        SecretKeySpec spec = generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, spec);
        byte[] encVal = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);
    }

    public static String decrypt(String strEncrypted) throws Exception {
        SecretKeySpec spec = generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, spec);
        byte[] decodeVal = Base64.decode(strEncrypted, Base64.DEFAULT);
        byte[] decVal = cipher.doFinal(decodeVal);
        return new String(decVal);
    }

    private static SecretKeySpec generateKey() throws Exception {
        String password = "apposqr";
        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        messageDigest.update(bytes);
        byte[] key = messageDigest.digest();
        return new SecretKeySpec(key, "AES");
    }
}
