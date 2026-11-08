package kh.edu.istad.platform.iam.infrastructure.config;

import kh.edu.istad.platform.iam.domain.dto.AuthUserResult;
import kh.edu.istad.platform.iam.domain.dto.FederatedIdentityCommand;
import kh.edu.istad.platform.iam.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest)
            throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId().toUpperCase();

        FederatedIdentityCommand command = customOAuth2UserService
                .extractCommand(registrationId, oidcUser.getAttributes());

        AuthUserResult authUser = findOrProvisionFederatedUserUseCase.execute(command);
        return new CustomOAuth2User(
                authUser,
                oidcUser.getAttributes(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo()
        );
    }
}
