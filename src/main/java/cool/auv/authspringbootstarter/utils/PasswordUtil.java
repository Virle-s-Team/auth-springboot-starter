package cool.auv.authspringbootstarter.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.HexFormat;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

@Slf4j
public class PasswordUtil {

    private static final String ALGORITHM = "PBEWithMD5AndDES"; // 可考虑升级为 PBKDF2WithHmacSHA256
    private static final int ITERATION_COUNT = 10000;           // 增加迭代次数以提升安全性
    private static final Cipher CIPHER;

    static {
        try {
            CIPHER = Cipher.getInstance(ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Cipher", e);
        }
    }

    public static byte[] getSalt() throws Exception {
        return new SecureRandom().generateSeed(8);
    }

    private static Key getPBEKey(String password) throws Exception {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        return keyFactory.generateSecret(keySpec);
    }

    public static String encrypt(String plaintext, String password, byte[] salt) throws Exception {
        Key key = getPBEKey(password);
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        synchronized (CIPHER) {
            CIPHER.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            byte[] encipheredData = CIPHER.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return bytesToHexString(encipheredData);
        }
    }

    public static String decrypt(String ciphertext, String password, byte[] salt) throws Exception {
        Key key = getPBEKey(password);
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        synchronized (CIPHER) {
            CIPHER.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            byte[] passDec = CIPHER.doFinal(hexStringToBytes(ciphertext));
            return passDec != null ? new String(passDec, StandardCharsets.UTF_8) : null;
        }
    }

    public static String bytesToHexString(byte[] src) {
        return src == null || src.length == 0 ? null : HexFormat.of().formatHex(src);
    }

    public static byte[] hexStringToBytes(String hexString) {
        return hexString == null || hexString.isEmpty() ? null : HexFormat.of().parseHex(hexString);
    }
}