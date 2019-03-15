package spring.boot.hr.sys.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring.boot.hr.sys.base.BaseModel;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseModel implements UserDetails {

    private String username;
    private String password;
    private String name;
    private String phone;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密钥是否过期
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户账号是否可用
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.delFlag.equals("0");
    }
}
