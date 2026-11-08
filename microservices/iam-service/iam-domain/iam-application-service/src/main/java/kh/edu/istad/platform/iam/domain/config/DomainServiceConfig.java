package kh.edu.istad.platform.iam.domain.config;

import kh.edu.istad.platform.iam.domain.service.IamDomainService;
import kh.edu.istad.platform.iam.domain.service.IamDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServiceConfig {

    @Bean
    public IamDomainService iamDomainService() {
        return new IamDomainServiceImpl();
    }
}
