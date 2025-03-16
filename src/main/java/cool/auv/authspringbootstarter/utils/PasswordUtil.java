package cool.auv.authspringbootstarter.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.HexFormat;

public class PasswordUtil {

    private static final String KEY_ALGORITHM = "PBKDF2WithHmacSHA256"; // 密钥派生算法
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"; // 加密算法
    private static final int ITERATION_COUNT = 10000; // 迭代次数
    private static final int KEY_LENGTH = 256; // 密钥长度（位）
    private static final Cipher CIPHER;

    static {
        try {
            CIPHER = Cipher.getInstance(CIPHER_ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Cipher", e);
        }
    }

    // 生成随机的密钥（返回明文密钥）
    public static String generateKey() {
        byte[] randomBytes = new SecureRandom().generateSeed(16); // 生成 16 字节随机数据
        return bytesToHexString(randomBytes).substring(0, 16);   // 取前 16 个字符作为密钥
    }

    // 生成随机的盐值（返回 String 类型）
    public static String generateSalt() {
        byte[] salt = new SecureRandom().generateSeed(16); // 生成 16 字节盐值
        return bytesToHexString(salt);
    }

    // 生成随机的初始化向量（IV）
    public static String generateIV() {
        byte[] iv = new SecureRandom().generateSeed(16); // 生成16 字节的AES IV
        return bytesToHexString(iv);
    }

    // 从明文密钥生成 AES 密钥
    private static SecretKey deriveKey(String key, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(key.toCharArray(), hexStringToBytes(salt), ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    // 加密方法
    public static String encrypt(String plaintext, String key, String salt, String iv) throws Exception {
        SecretKey secretKey = deriveKey(key, salt);
        IvParameterSpec ivSpec = new IvParameterSpec(hexStringToBytes(iv));
        synchronized (CIPHER) {
            CIPHER.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedData = CIPHER.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return bytesToHexString(encryptedData);
        }
    }

    // 解密方法
    public static String decrypt(String ciphertext, String key, String salt, String iv) throws Exception {
        SecretKey secretKey = deriveKey(key, salt);
        IvParameterSpec ivSpec = new IvParameterSpec(hexStringToBytes(iv));
        synchronized (CIPHER) {
            CIPHER.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decryptedData = CIPHER.doFinal(hexStringToBytes(ciphertext));
            return decryptedData != null ? new String(decryptedData, StandardCharsets.UTF_8) : null;
        }
    }

    public static String bytesToHexString(byte[] src) {
        return src == null || src.length == 0 ? null : HexFormat.of().formatHex(src);
    }

    public static byte[] hexStringToBytes(String hexString) {
        return hexString == null || hexString.isEmpty() ? null : HexFormat.of().parseHex(hexString);
    }
}