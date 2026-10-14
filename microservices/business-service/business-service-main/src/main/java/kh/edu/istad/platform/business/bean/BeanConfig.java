package kh.edu.istad.platform.business.bean;

import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.business.domain.usecase.RegisterBusinessUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public RegisterBusinessUseCase registerBusinessUseCase(
            BusinessRepository businessRepository
    ) {
        return new RegisterBusinessUseCase(businessRepository);
    }
}
