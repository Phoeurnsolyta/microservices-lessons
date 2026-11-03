package kh.edu.istad.platform.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.support.OAuth2WebClientHttpServiceGroupConfigurer;

@Configuration
@ClientRegistrationId(registrationId = "service-client")
public class OAuth2ClientConfig {

    @Bean
    public ReactiveOAuth2AuthorizedClientService authorizedClientService(
            ReactiveClientRegistrationRepository repository
    ) {
        return new InMemoryReactiveOAuth2AuthorizedClientService(repository);
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager authorizedClientManager(
            ReactiveClientRegistrationRepository repository,
            ReactiveOAuth2AuthorizedClientService service
    ) {
        var manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        repository,
                        service
                );
        manager.setAuthorizedClientProvider(
                ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build()
        );
        return manager;
    }

    @Bean
    OAuth2WebClientHttpServiceGroupConfigurer securityConfigurer(
            ReactiveOAuth2AuthorizedClientManager manager) {
        return OAuth2WebClientHttpServiceGroupConfigurer.from(manager);
    }
}
