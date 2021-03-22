package com.shangan.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author Alva
 * @CreateTime 2021/1/28 15:28
 * JWT token 工具类
 * function
 * 1.生成 JWT token
 * 2.判断 token 是否有效，参数: String
 * 3.判断 token 是否有效，参数: HttpServletRequest
 * 4.从 token 字符串获取用户的 id，参数: HttpServletRequest
 */
public class JwtUtil {

    /**
     * 常量 EXPIRE: 过期时间 24 小时
     * 常量 APP_SECRET: 自定义密钥
     */
    public static final long EXPIRE = 1000 * 60 * 60 * 24;
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    /**
     * 生成 token 字符串的方法
     * JWT 协议:
     * Header: 类型和加密算法
     * Body: token 的过期时间 + 用户信息
     * Tail: 哈希签名，防伪标志 (验证 JWT)
     * @param id
     * @param nickname
     * @return
     */
    public static String getJwtToken(Long id, String nickname){
//        构建 JWT token 字符串
        return Jwts.builder()
//                设置 toeken 的 Header 信息
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
//                设置 token 的过期时间
                .setSubject("shangan-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
//                设置 token 的主体
                .claim("user_id", id)
                .claim("nick_name", nickname)
//                哈希签名，防伪标志
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
//                拼接以上各段
                .compact();
    }

    /**
     * 判断 token 是否存在与有效 (参数为 String)
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
//        token 为空则返回
        if (StringUtils.isEmpty(jwtToken)) return false;
//        判断是否有效，返回判断后的 Boolean
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 判断 token 是否存在与有效 (参数为 HttpServletRequest)
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
//        通过 request 获取 Header 中 token 字段的值
        try {
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 根据 token 获取用户 id (参数 HttpServletRequest)
     * @param request
     * @return
     */
//    public static String getMemberIdByJwtToken(HttpServletRequest request) {
//        String jwtToken = request.getHeader("token");
//        if(StringUtils.isEmpty(jwtToken)) return "";
//        Jws<Claims> claimsJws =
//                Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
////        获取 JWT token 的主体: 包含过期时间和用户信息
//        Claims claims = claimsJws.getBody();
////        提取出用户的 id 字段
//        return (String)claims.get("user_id");
//    }
}
