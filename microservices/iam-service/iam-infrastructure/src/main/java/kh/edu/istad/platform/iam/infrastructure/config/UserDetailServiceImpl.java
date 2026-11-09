package kh.edu.istad.platform.iam.infrastructure.config;

import kh.edu.istad.platform.iam.domain.dto.AuthUserResult;
import kh.edu.istad.platform.iam.domain.usecase.user.LoadUserForAuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final LoadUserForAuthenticationUseCase loadUserForAuthenticationUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AuthUserResult authUser = loadUserForAuthenticationUseCase.execute(username);

            Set<GrantedAuthority> authorities = new HashSet<>();
            if (authUser.authorities() != null) {
                for (String auth : authUser.authorities()) {
                    authorities.add(new SimpleGrantedAuthority(auth));
                }
            }

            return User.builder()
                    .username(authUser.username())
                    .password(authUser.passwordHash() != null ? authUser.passwordHash() : "")
                    .disabled(!authUser.enabled())
                    .accountExpired(!authUser.accountNonExpired())
                    .accountLocked(!authUser.accountNonLocked())
                    .credentialsExpired(!authUser.credentialsNonExpired())
                    .authorities(authorities)
                    .build();
        } catch (Exception ex) {
            throw new UsernameNotFoundException("User not found: " + username, ex);
        }
    }
}