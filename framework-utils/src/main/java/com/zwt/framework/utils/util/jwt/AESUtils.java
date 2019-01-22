package com.zwt.framework.utils.util.jwt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author zwt
 * @detail
 * @date 2019/1/21
 * @since 1.0
 */
@Slf4j
public class AESUtils {
    private final static String ENCODING_UTF8 = "utf-8";
    private static final String KEY_ALGORITHM = "AES";
    /**默认的加密算法*/
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private String secretKeySeed = null;
    private String ivParameterSeed = null;
    public AESUtils(String secretKeySeed,String ivParameterSeed) {
        this.secretKeySeed = secretKeySeed;
        if (ivParameterSeed.length() != 16) {
            throw new RuntimeException("iv向量长度必须为16");
        }
        this.ivParameterSeed=ivParameterSeed;
    }
    public AESUtils(String secretKeySeed) {
        if (secretKeySeed.length() != 16) {
            throw new RuntimeException("iv向量长度必须为16");
        }
        this.secretKeySeed = secretKeySeed;
        this.ivParameterSeed=secretKeySeed;
    }
    /**
     * AES加密
     * @param content
     * @return String
     */
    public String aesEncrypt(String content) throws Exception {
        // AES加密
        byte[] encryptStr = encrypt(content, secretKeySeed,ivParameterSeed);
        // BASE64位加密
        return Base64.encodeBase64String(encryptStr);
    }
    /**
     * AES解密
     * @param encryptStr
     * @return String
     */
    public String aesDecrypt(String encryptStr) throws Exception {
        // BASE64位解密
        byte[] decodeBase64 = Base64.decodeBase64(encryptStr);
        // AES解密
        return new String(decrypt(decodeBase64, secretKeySeed,ivParameterSeed),ENCODING_UTF8);
    }

    /**
     * 生成加密秘钥
     * @return
     */
    private static SecretKeySpec getSecretKeySpec(final String secretKeySeed) {
        try {
            return new SecretKeySpec(secretKeySeed.getBytes(), "AES");
        } catch (Exception ex) {
            log.error("生成加密密钥异常",ex);
        }
        return null;
    }

    /**
     * 生成向量秘钥
     * @return
     */
    private static IvParameterSpec getIvParameterSpec(final String ivParameterSeed) {
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        return new IvParameterSpec(ivParameterSeed.getBytes());
    }

    /**
     * 加密
     * @param content
     * @return byte[]
     */
    private static byte[] encrypt(String content, String secretKeySeed,String ivParameterSeed) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化为加密模式的密码器
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(secretKeySeed),getIvParameterSpec(ivParameterSeed));
        // 加密
        return cipher.doFinal(content.getBytes(ENCODING_UTF8));
    }
    /**
     * 解密
     * @param content
     * @return byte[]
     */
    private static byte[] decrypt(byte[] content, String secretKeySeed,String ivParameterSeed) throws Exception {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 初始化为加密模式的密码器
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(secretKeySeed),getIvParameterSpec(ivParameterSeed));
        // 解密
        return cipher.doFinal(content);
    }
}
