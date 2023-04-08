package site.surui.web.common.util;


import io.jsonwebtoken.*;
import site.surui.web.common.data.po.User;
import site.surui.web.common.data.vo.result.AuthError;
import site.surui.web.common.data.vo.result.Result;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

public class JwtUtil {
    public static final String JWT_SECRET = "dfb70cce5939b7023d0ca97b86937bf9";
    public static final Long TTL_TIME = (long) (1000 * 60 * 60 * 24);

    /**
     * 签发JWT
     *
     * @param id        WT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
     * @param user   内容
     * @param ttlMillis 有效时间
     * @return String
     */
    public static String generateJWT(String id, User user, Long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();

        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(user.getUserId())
                .setHeaderParam("typ", "JWT")
                .claim("user_id", user.getUserId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(SignatureAlgorithm.HS256
                        , secretKey);
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }

        return builder.compact();
    }

    /**
     * 验证JWT
     *
     * @param jwtStr JWT字符串
     * @return Result
     */
    public static Result<?> validateJWT(String jwtStr) {
        Result<?> result ;
        try {
            Claims claims = parseJWT(jwtStr);
            result = new Result<>().success("成功", claims);
        } catch (ExpiredJwtException e) {
            result = new Result<>().fail(AuthError.LOGIN_STATUS_IS_INVALID);
        } catch (Exception e) {
            result = new Result<>().fail(AuthError.LOGIN_STATUS_WRONG);
        }
        return result;
    }


    private static SecretKey generalKey() {
        byte[] encodedKey = Base64Util.decodeStringToByte(JWT_SECRET);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 解析JWT字符串
     *
     * @param jwtStr JWT字符串
     * @return Claims
     */
    public static Claims parseJWT(String jwtStr) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwtStr)
                .getBody();
    }
}
