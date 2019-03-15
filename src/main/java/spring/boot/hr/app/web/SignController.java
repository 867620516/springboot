package spring.boot.hr.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.hr.sys.security.User;
import spring.boot.hr.sys.security.UserService;

@RestController
public class SignController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注册用户信息
     * @param user
     * @return
     */
    @PostMapping("/api/signUp")
    public User signUp(@RequestBody User user){
        //存储注册信息
        //把明文密码加密成字符串
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return user;
    }

    /**
     * 当拦截登陆请求并且权限验证通过后，访问该方法返回success信息
     * @return
     */
    @PostMapping("/api/signIn")
    public String signInSuccessHandler(){return "{\"status\":\"success\"}";}

    @RequestMapping("/api/haha")
    public String haha(){
        return "{\"content\":\"haha\"}";
    }

}
