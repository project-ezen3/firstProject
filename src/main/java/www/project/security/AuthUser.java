package www.project.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import www.project.domain.UserVO;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthUser extends User {
    private UserVO userVO;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthUser(UserVO userVO){
        super(userVO.getEmail(), userVO.getPw(), userVO.getAuthList().stream().map(
                authVO -> new SimpleGrantedAuthority(authVO.getRole())).collect(Collectors.toList()));
        this.userVO = userVO;
    }
}
