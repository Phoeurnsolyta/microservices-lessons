package kh.edu.istad.platform.iam.infrastructure.config;

import kh.edu.istad.platform.iam.persistence.repository.UserJpaRepository;
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

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userJpaRepository.findByUsernameOrEmail(username, username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );

        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getPermissions()
                .forEach(
                        permission -> {
                            authorities.add(
                                    new SimpleGrantedAuthority(permission.getPermission())
                            );
                        }
                );

        user.getRoles()
                .forEach(
                        role -> {
                            authorities.add(
                                    new SimpleGrantedAuthority("ROLE_" + role.getRole())
                            );
                        }
                );

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                authorities
        );
    }
}
