package spring.boot.hr.sys.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.boot.hr.sys.utils.JWTUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;



    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    /**
     * 从请求中获取username和password并且组装成Authentication对象
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //前后端分离的架构，所以不再进行表单提交 而是json数据的直接提交content-type = application/json {username：？，password：？}
        //需要将接送字符串转换成User对象（UserDetails对象）
        try {
            //需要通过jackson将json数据转换成User对象
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            //直接返回Authentication对象，实现类为UsernamePasswordAuthentication
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>());

            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            return authenticate;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);//抛异常代表提交的数据格式有误，无法进行权限验证
        }
    }

    /**
     * 权限验证通过后，将jwt token放入响应头中
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //1.生成jwt token
        String token = JWTUtils.generateJWT(authResult.getName(), authResult.getAuthorities());
        //2.放入响应头中
        response.addHeader(HttpHeaders.AUTHORIZATION,token);

    }
}
