package kh.edu.istad.platform.business.persistence.config;

import kh.edu.istad.platform.business.persistence.convert.BusinessIdReadConverter;
import kh.edu.istad.platform.business.persistence.convert.BusinessIdWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions bean () {
        return MongoCustomConversions.create(
                adapter -> {
                    adapter.registerConverter(new BusinessIdReadConverter());
                    adapter.registerConverter(new BusinessIdWriteConverter());

                }
        );
    }
}
