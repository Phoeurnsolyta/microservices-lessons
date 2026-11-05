package kh.edu.istad.platform.order.client.config;

import io.grpc.Channel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class CustomerGrpcClientConfig {

    @Bean
    public Channel customerServiceChannel(
            GrpcChannelFactory channelFactory
    ) {
        return channelFactory.createChannel("customer-service");
    }
}
