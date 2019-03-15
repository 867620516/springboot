package spring.boot.hr.sys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spring.boot.hr.sys.security.JWTAuthenticationFilter;
import spring.boot.hr.sys.security.JWTLoginFilter;
import spring.boot.hr.sys.security.UserService;
import spring.boot.hr.sys.utils.JWTUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity //启动spring security的web支持
public class AppConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    @Autowired
    private UserService userService;
    @Autowired
    private Environment env;
    /**
     * 全局配置跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  //哪些请求需要跨域
                .allowedOrigins("*")    //代表允许所有的地址和端口对本服务进行访问
                .allowedHeaders("token")    //允许请求中有哪些自定义的请求头
                .exposedHeaders("token") //返回的响应头
                .allowCredentials(true)  //是否允许密钥
                .allowedMethods("DELETE","POST","GET","PUT") //允许请求方法
                .maxAge(3600);//请求超时时间1小时，以秒为单位
    }

    /**
     * 配置JWTUtils的静态属性
     * @return
     */
    @Bean
    @Lazy(false)//不允许懒加载
    public int readJWTConfig(){
        JWTUtils.setExpire(env.getProperty("JWT.expire",Long.class,6000000L));
        JWTUtils.setSecret(env.getProperty("JWT.secret"));
        return 1; //1代表优先级最高
    }

    /**
     * 配置spring security的权限验证规则
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter(authenticationManager());

        //设置登陆请求地址
        jwtLoginFilter.setFilterProcessesUrl("/api/signIn");
        /*jwtLoginFilter.setAuthenticationSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("{\"status\":\"success\"}");
        });*/

        http.cors() //允许跨域
            .and() //and代表上一个配置完了后回到父节点继续进行配置
            .httpBasic().disable() //不允许进行httpbasic的验证
            .csrf().disable()       //关闭csrf
            .authorizeRequests()    //设置不同路径的权限验证规则
            .antMatchers("/api/signUp").permitAll() //对/api/signUp进行放行
            .anyRequest().authenticated()//除了以上放行的路径都必须进行权限验证
            .and().addFilter(jwtLoginFilter) //第一个filter用于登陆权限控制
            .addFilter(new JWTAuthenticationFilter(authenticationManager())); //第二个filter用于验证登陆后用户的具体权限
        // 两个filter都不需要实现验证逻辑，而只是对JWT的序列化和反序列化
    }

    /**
     * 配置加密算法
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 配置userService
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //构造AuthenticationManager，设置manager的userDetailsService实现以及密码加密器的实现
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());

    }
}
