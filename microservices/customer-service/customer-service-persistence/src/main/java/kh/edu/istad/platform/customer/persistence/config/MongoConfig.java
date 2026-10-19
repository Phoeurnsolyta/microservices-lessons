package kh.edu.istad.platform.customer.persistence.config;

import kh.edu.istad.platform.customer.persistence.convert.CustomerReadConverter;
import kh.edu.istad.platform.customer.persistence.convert.CustomerWriteConverter;
import org.bson.UuidRepresentation;
import org.springframework.boot.mongodb.autoconfigure.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
public class MongoConfig {

//    @Bean
//    public MongoCustomConversions bean() {
//        return MongoCustomConversions.create(
//                adapter -> {
//                    adapter.registerConverter(new CustomerReadConverter());
//                    adapter.registerConverter(new CustomerWriteConverter());
//                }
//        );
//    }

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoClientSettingsBuilderCustomizer() {
        return settingsBuilder ->
                settingsBuilder.uuidRepresentation(UuidRepresentation.STANDARD);
    }
}
