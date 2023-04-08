package site.surui.web.common.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

/**
 * AES工具类，密钥必须是16位字符串
 */
@Component
public class AESUtil {

    /**
     * 偏移量,必须是16位字符串
     */
    // @Value("${aes.offset_string}")
    private final String offsetString = "0392039203920300";

    /**
     * 默认的密钥(必须为16位)
     */
    // @Value("${aes.key}")
    private final String DEFAULT_KEY = "0392039203920300";

    /**
     * 产生随机密钥(这里产生密钥必须是16位)
     */
    public static String generateKey() {
        String key = UUID.randomUUID().toString();
        key = key.replace("-", "").substring(0, 16);// 替换掉-号
        return key;
    }

    /**
     * 加密(使用学号加盐)
     */
    public String encryptDataWithCasId(String casId, String content) {
        return encryptData(md5(DEFAULT_KEY, casId).substring(4, 20), content);
    }

    /**
     * 解密(使用学号加盐)
     */
    public String decryptDataWithCasId(String casId, String content) {
        return decryptData(md5(DEFAULT_KEY, casId).substring(4, 20), content);
    }


    private String encryptData(String key, String content) {
        byte[] encryptedBytes;
        try {
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            // 注意，为了能与 iOS 统一
            // 这里的 key 不可以使用 KeyGenerator、SecureRandom、SecretKey 生成
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = offsetString.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            // 指定加密的算法、工作模式和填充方式
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            encryptedBytes = cipher.doFinal(byteContent);
            // 同样对加密后数据进行 base64 编码
            return Base64Util.encodeByteToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String decryptData(String key, String content) {
        try {
            // base64 解码
            byte[] encryptedBytes = Base64Util.decodeStringToByte(content);
            byte[] enCodeFormat = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(enCodeFormat, "AES");
            byte[] initParam = offsetString.getBytes();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] result = cipher.doFinal(encryptedBytes);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String md5(Object... args) {
        String data = Arrays.toString(args);
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                if ((b & 0xff) < 16)
                    sb.append("0");
                sb.append(Long.toString(b & 0xff, 16));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}