package spring.boot.hr.sys.utils;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JWTUtils {
    public final static String BEARER = "Bearer  ";
    private static String secret;
    private static Long expire;

    public static void setSecret(String secret) {
        JWTUtils.secret = secret;
    }

    public static void setExpire(Long expire) {
        JWTUtils.expire = expire;
    }

    /**
     * 生成jwt token
     * @param subject 代表用户名
     * @param authorities 代表用户的权限集合
     * @return
     */
    public static String generateJWT(String subject, Collection<? extends GrantedAuthority> authorities){
        /*//将authorities转换成字符串
        String[] strArr = new String[authorities.size()];
        Object[] authObjArr =  authorities.toArray();
        for(int i =0;i<authObjArr.length;i++){
            GrantedAuthority authority = (GrantedAuthority) authObjArr[i];
            strArr[i] = authority.getAuthority();//获得权限标识符
        }
        String authStr = String.join(",", strArr);*/

        /*jdk8  Stream api 写法*/
        String collect
                = authorities.stream()
                .map(GrantedAuthority.class::cast)//map将原有集合的类型转换成GrantedAuthority集合
                .map(GrantedAuthority::getAuthority)  //通过GrantedAuthority的getAuthority方法将集合转换成string数组
                .collect(Collectors.joining(","));

        //创建jwt的构造器对象JWTClaimsSet
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        builder.subject(subject)//将用户名设置为subject
                .issuer("boothr") //将项目名称设置为发布者
                .expirationTime(new Date(new Date().getTime() + expire)) //设置过期时间
                .claim("roles",collect);  //声明自定义的载荷：保存角色信息转为JSON对象

        //通过构造器生成jwt对象
        JWTClaimsSet claimsSet = builder.build();

        //创建签名对象
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        try {
            signedJWT.sign(new MACSigner(secret));
        } catch (JOSEException e) {
            e.printStackTrace();
        }

        return signedJWT.serialize();
    }

    /**
     * 从request中获取token并且进行解析，获得用户名以及权限并且
     * 将这些信息封装成spring security能够识别的
     * Authentication对象
     * @param request
     * @return
     */
    public static Authentication extractJWT(HttpServletRequest request){
        //从request中获得请求头为Authorization的token字符串："bearer qwertyuiop..."
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        if(StringUtils.isNotBlank(authHeader)){
            //不为空，对token字符串进行去bearer
            token = authHeader.substring(BEARER.length()-1);

            //解密
            try {
                //将字符串token反序列化成jwt对象
                SignedJWT signedJWT = SignedJWT.parse(token);
                //判断jwt是否过期并且是否合法
                //获得jwt的过期时间，判断是否超过当前系统时间
                boolean isAfter = signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());
                //检查你的签名是否合法
                boolean isVerify = signedJWT.verify(new MACVerifier(secret));
                //判断jwt是否过期并且是否合法,然后在进行解密
                if(isAfter && isVerify){
                    //提取其中关于user的信息 username 和authority
                    String subject;
                    String authStr;
                    List<GrantedAuthority> authorities;

                    subject = signedJWT.getJWTClaimsSet().getSubject();

                    authStr = (String) signedJWT.getJWTClaimsSet().getClaim("roles");
                    //将authStr转换成GrantedAuthority集合
                    authorities = Stream.of(authStr.split(","))
                            .filter(StringUtils::isNoneBlank) //排除空字符串
                            .map(SimpleGrantedAuthority::new) //转换成GrantedAuthority集合
                            .collect(Collectors.toList());


                    //将subject和authorities组成Authentication对象 UsernamePasswordAuthentication
                    return new UsernamePasswordAuthenticationToken(subject,null,authorities);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JOSEException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
