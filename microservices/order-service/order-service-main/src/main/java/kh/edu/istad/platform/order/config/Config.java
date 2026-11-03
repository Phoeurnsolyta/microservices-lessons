package kh.edu.istad.platform.order.config;

import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.security.oauth2.client.web.reactive.function.client.support.OAuth2WebClientHttpServiceGroupConfigurer;

@ClientRegistrationId(registrationId = "service-client")
public class Config {

    OAuth2WebClientHttpServiceGroupConfigurer oauth2WebClientHttpServiceGroupConfigurer;
}
