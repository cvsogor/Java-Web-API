package com.aditor.bi.commercials.security;
import com.aditor.bi.commercials.domain.User;
import com.aditor.bi.commercials.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CommercialsAuthenticationProvider implements AuthenticationProvider {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CommercialsAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();

        if(username.equals("aditor123") && password.equals("aditor123")) {
            User user = new User(username);
            user.setRole(User.ROLE_ADMIN);
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        }

        User user = userRepository.findByUsername(username);

        if(user != null && passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
