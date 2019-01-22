package com.zwt.framework.utils.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author zwt
 * @detail jwt生成token
 * @date 2019/1/21
 * @since 1.0
 */
@Slf4j
public class JWTUtils {
    //JWT header
    private String JWT_HEADER = "";
    //签名算法
    private Algorithm algorithm;
    //默认token过期时间
    private long expireTimeMillis=2*60*60*1000;
    private AtomicBoolean initState = new AtomicBoolean(false);

    private static AESUtils aesUtils=null;

    private static class SingletonHolder {
        static final JWTUtils instance = new JWTUtils();
    }

    public static JWTUtils getInstance() {
        return JWTUtils.SingletonHolder.instance;
    }

    /**
     * 初始化jwt
     * @param jwtSecretKey
     * @param jwtExpireTimeSeconds
     * @param aesSecretKeySeed
     * @param aesIvParameterSeed
     * @throws UnsupportedEncodingException
     */
    public void init(String jwtSecretKey,long jwtExpireTimeSeconds,String aesSecretKeySeed,String aesIvParameterSeed) throws UnsupportedEncodingException {
        if (initState.compareAndSet(false, true)) {
            this.algorithm = Algorithm.HMAC256(jwtSecretKey);
            if(jwtExpireTimeSeconds>0) {
                this.expireTimeMillis = jwtExpireTimeSeconds * 1000;
            }
            this.JWT_HEADER = StringUtils.substringBefore(JWT.create().sign(Algorithm.HMAC256(jwtSecretKey)), ".")+".";
            aesUtils = new AESUtils(aesSecretKeySeed, aesIvParameterSeed);
        }else{
            log.error("重复初始化jwt");
        }
    }

    /**
     * 使用jwt生成token
     * @param userVo
     * @return
     */
    public String encodeJWT(UserVo userVo) {
        return encodeJWT(userVo,this.expireTimeMillis);
    }

    /**
     * 生成token
     * @param userVo
     * @param expireTimeMillis
     * @return
     */
    public String encodeJWT(UserVo userVo,long expireTimeMillis){
        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis()+expireTimeMillis))
                .withClaim("phone",userVo.getPhone())
                .withClaim("userId",userVo.getUserId())
                .withClaim("sessionId",userVo.getSessionId())
                .withClaim("platform",userVo.getPlatform())
                .sign(algorithm);
        System.out.println("token----> "+token);
        try {
            token = aesUtils.aesEncrypt(StringUtils.removeStart(token, JWT_HEADER));
        } catch (Exception ex) {
            log.error("加密异常",ex);
            token = "";
        }
        return token;
    }

    /**
     * 解密token
     * @param token
     * @return
     */
    public UserVo decodeJWT(String token) {
        UserVo userVo =new UserVo();
        try {
            if (StringUtils.isBlank(token)) {
                throw new RuntimeException("无效token");
            }
            String decryptJwtToken = aesUtils.aesDecrypt(token);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(StringUtils.join(JWT_HEADER,decryptJwtToken));
            long expiresAt = jwt.getExpiresAt()==null?0:jwt.getExpiresAt().getTime();
            if(System.currentTimeMillis()>expiresAt){
                throw new RuntimeException("token有效期超期");
            }
            Map<String, Claim> claims = jwt.getClaims();
            userVo.setPhone(claims.get("phone")==null?"":claims.get("phone").asString());
            userVo.setUserId(claims.get("userId")==null?"":claims.get("userId").asString());
            userVo.setSessionId(claims.get("sessionId")==null?"":claims.get("sessionId").asString());
            userVo.setPlatform(claims.get("platform")==null?"":claims.get("platform").asString());
            userVo.setExpiresAt(expiresAt);

        } catch (Exception exception){
            throw new RuntimeException("无效token");
        }
        return userVo;
    }

    public static void main(String[] args) throws Exception{
        JWTUtils jwtUtils = JWTUtils.getInstance();
        jwtUtils.init("sakuratears",1000,"1234567891111111","test111111111111");
        UserVo userVo = new UserVo();
        userVo.setUserId("1433223").setPhone("1888888888").setPlatform("APP").setSessionId("111111111111");
        String token = jwtUtils.encodeJWT(userVo);
        System.out.println("加密token----> "+token);
        Thread.sleep(1);
        UserVo vo = jwtUtils.decodeJWT(token);
        System.out.println("解密token得到结果----> "+vo.toString());
    }
}
