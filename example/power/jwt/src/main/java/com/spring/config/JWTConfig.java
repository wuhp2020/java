package com.spring.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @ Author : wuheping
 * @ Date   : 2022/5/14
 * @ Desc   : 描述
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {

    // 定义token返回头部
    @Value("${header}")
    public String header;

    // token前缀
    @Value("${tokenPrefix}")
    public String tokenPrefix;

    // 签名密钥
    @Value("${secret}")
    public String secret;

    // 有效期
    @Value("${expireTime}")
    public long expireTime;

    // 存进客户端的token的key名
    public static final String USER_LOGIN_TOKEN = "USER_LOGIN_TOKEN";

    /**
     * 创建TOKEN
     * @param sub
     * @return
     */
    public String createToken(String sub) {
        return tokenPrefix + JWT.create()
                .withSubject(sub)
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .sign(Algorithm.HMAC512(secret));
    }


    /**
     * 验证token
     * @param token
     */
    public String validateToken(String token) throws Exception {
        try {
            return JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getSubject();
        } catch (TokenExpiredException e) {
            throw new Exception("token已经过期");
        } catch (Exception e){
            throw new Exception("token验证失败");
        }
    }

    /**
     * 检查token是否需要更新
     * @param token
     * @return
     */
    public boolean isNeedUpdate(String token) throws Exception {
        // 获取token过期时间
        Date expiresAt = null;
        try {
            expiresAt = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getExpiresAt();
        } catch (TokenExpiredException e) {
            return true;
        } catch (Exception e) {
            throw new Exception("token验证失败");
        }
        // 如果剩余过期时间少于过期时常的一般时 需要更新
        return (expiresAt.getTime()-System.currentTimeMillis()) < (expireTime>>1);
    }
}
