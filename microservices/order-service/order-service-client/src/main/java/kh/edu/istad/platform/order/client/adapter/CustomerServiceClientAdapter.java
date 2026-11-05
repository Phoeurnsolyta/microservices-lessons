package kh.edu.istad.platform.order.client.adapter;

import io.grpc.Channel;
import io.grpc.ClientInterceptors;
import io.grpc.stub.StreamObserver;
import kh.edu.istad.platform.customer.contract.CustomerServiceGrpc;
import kh.edu.istad.platform.customer.contract.GetCustomerByIdRequest;
import kh.edu.istad.platform.customer.contract.GetCustomerByIdResponse;
import kh.edu.istad.platform.order.client.config.GrpcBearerTokenInterceptor;
import kh.edu.istad.platform.order.client.httpclient.CustomerHttpClient;
import kh.edu.istad.platform.order.domain.dto.QueryCustomerResult;
import kh.edu.istad.platform.order.domain.port.out.CustomerServiceClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerServiceClientAdapter implements CustomerServiceClient {

    private final WebClient.Builder webClientBuilder;
    private final CustomerHttpClient customerHttpClient;
    private final Channel customerServiceChannel;
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;
    private static final Logger log =
            LoggerFactory.getLogger(CustomerServiceClientAdapter.class);

    private record ValuePayload(String value) {
    }

    private record CustomerPayload(
            ValuePayload id,
            String username,
            String familyName,
            String givenName,
            ValuePayload email,
            ValuePayload phoneNumber,
            String status
    ) {
    }

    @Override
    public Flux<QueryCustomerResult> getCustomerName() {
        return customerHttpClient.queryCustomers();
    }

    @Override
    public Flux<CustomerResponse> getCustomers() {
        return webClientBuilder
                .baseUrl("http://customer-service")
                .build()
                .get()
                .uri("/api/customers")
                .retrieve()
                .bodyToFlux(CustomerPayload.class)
                .map(customer -> new CustomerResponse(
                        customer.id().value(),
                        customer.username(),
                        customer.familyName(),
                        customer.givenName(),
                        customer.email().value(),
                        customer.phoneNumber().value(),
                        customer.status(),
                        null
                ));
    }

    @Override
    public Mono<CustomerDetails> getCustomerById(String customerId) {

        log.info("Calling Customer Service through gRPC: customerId={}",
                customerId);

        GetCustomerByIdRequest request =
                GetCustomerByIdRequest.newBuilder()
                        .setCustomerId(customerId)
                        .build();

        OAuth2AuthorizeRequest authorizeRequest =
                OAuth2AuthorizeRequest
                        .withClientRegistrationId("service-client")
                        .principal("order-service")
                        .build();

        return authorizedClientManager.authorize(authorizeRequest)
                .switchIfEmpty(Mono.error(
                        new IllegalStateException("Unable to obtain OAuth2 access token")
                ))
                .flatMap(authorizedClient -> {

                    log.info("OAuth2 access token obtained successfully");

                    String accessToken = authorizedClient
                            .getAccessToken()
                            .getTokenValue();

                    Channel authenticatedChannel =
                            ClientInterceptors.intercept(
                                    customerServiceChannel,
                                    new GrpcBearerTokenInterceptor(accessToken)
                            );

                    CustomerServiceGrpc.CustomerServiceStub stub =
                            CustomerServiceGrpc.newStub(authenticatedChannel);

                    return Mono.create(sink ->
                            stub.getCustomerById(
                                    request,
                                    new StreamObserver<>() {

                                        @Override
                                        public void onNext(GetCustomerByIdResponse response) {

                                            log.info(
                                                    "Received gRPC response: customerId={}, username={}",
                                                    response.getCustomerId(),
                                                    response.getUsername()
                                            );

                                            sink.success(new CustomerDetails(
                                                    response.getCustomerId(),
                                                    response.getUsername(),
                                                    response.getFamilyName(),
                                                    response.getGivenName(),
                                                    response.getEmail(),
                                                    response.getPhoneNumber(),
                                                    response.getStatus(),
                                                    response.getSegmentId()
                                            ));
                                        }

                                        @Override
                                        public void onError(Throwable throwable) {
                                            log.error(
                                                    "gRPC call failed: {}",
                                                    throwable.getMessage()
                                            );
                                            sink.error(throwable);
                                        }

                                        @Override
                                        public void onCompleted() {
                                            // Unary response already emitted.
                                        }
                                    }
                            )
                    );
                });
    }
}