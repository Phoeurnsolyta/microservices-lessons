package kh.edu.istad.platform.iam.infrastructure.config;

import kh.edu.istad.platform.iam.domain.dto.AuthUserResult;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class CustomOAuth2User implements OAuth2User, OidcUser, UserDetails, Serializable {

    private final Long id;
    private final UUID uuid;
    private final String username;
    private final String email;
    private final boolean enabled;
    private final Set<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;

    public CustomOAuth2User(
            AuthUserResult authUser,
            Map<String, Object> attributes,
            OidcIdToken idToken, OidcUserInfo userInfo) {

        this.id = authUser.id();
        this.uuid = authUser.uuid();
        this.username = authUser.username();
        this.email = authUser.email();
        this.enabled = authUser.enabled();
        this.attributes = attributes != null ? attributes : Map.of();
        this.idToken = idToken;
        this.userInfo = userInfo;
        this.authorities = authUser.authorities() != null
                ? authUser.authorities().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet())
                : Set.of();
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getClaims() {
        return attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
