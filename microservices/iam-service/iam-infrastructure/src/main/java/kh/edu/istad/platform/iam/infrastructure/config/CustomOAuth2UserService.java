package kh.edu.istad.platform.iam.infrastructure.config;

import kh.edu.istad.platform.iam.domain.dto.AuthUserResult;
import kh.edu.istad.platform.iam.domain.dto.FederatedIdentityCommand;
import kh.edu.istad.platform.iam.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;

    @Override
    public OAuth2User loadUser(
            OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId().toUpperCase();

        FederatedIdentityCommand command = extractCommand(
                registrationId, oauth2User.getAttributes()
        );
        AuthUserResult authUser = findOrProvisionFederatedUserUseCase.execute(command);

        return new CustomOAuth2User(
                authUser,
                oauth2User.getAttributes(),
                null, null
        );
    }

    public FederatedIdentityCommand extractCommand(
            String provider, Map<String, Object> attributes) {
        String providerSubject;

        String email = (String) attributes.get("email");
        boolean emailVerified = Boolean.TRUE.equals(attributes.get("email_verified"));
        String displayName = (String) attributes.get("name");
        String username = null;
        String pictureUrl = null;

        if (
                "GITHUB".equalsIgnoreCase(provider)
        ) {
            providerSubject = String.valueOf(attributes.get("id"));
            username = (String) attributes.get("login");
            pictureUrl = (String) attributes.get("avatar_url");

            if (displayName == null) displayName = username;
            if (email != null) emailVerified = true;
        } else if (
                "FACEBOOK".equalsIgnoreCase(provider)
        ) {
            providerSubject = String.valueOf(attributes.get("id")
            );
            pictureUrl = attributes
                    .get("picture") != null ? attributes
                    .get("picture").toString() : null;
        }
        else { // GOOGLE or generic
            providerSubject = String.valueOf(
                    attributes.getOrDefault("sub", attributes.get("id"))
            );

            pictureUrl = (String) attributes.get("picture");
            username = (String) attributes.get("preferred_username");
        }
        return new FederatedIdentityCommand(
                provider,
                providerSubject,
                email,
                emailVerified,
                displayName,
                username,
                pictureUrl,
                attributes
        );
    }
}
