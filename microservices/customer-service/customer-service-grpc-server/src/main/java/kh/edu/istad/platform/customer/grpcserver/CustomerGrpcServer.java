package kh.edu.istad.platform.customer.grpcserver;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kh.edu.istad.platform.customer.contract.CustomerServiceGrpc;
import kh.edu.istad.platform.customer.contract.GetCustomerByIdRequest;
import kh.edu.istad.platform.customer.contract.GetCustomerByIdResponse;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.usecase.GetCustomerByIdUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class CustomerGrpcServer extends CustomerServiceGrpc.CustomerServiceImplBase {

    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private static final Logger log =
            LoggerFactory.getLogger(CustomerGrpcServer.class);

    @Override
    public void getCustomerById(
            GetCustomerByIdRequest request,
            StreamObserver<GetCustomerByIdResponse> responseObserver
    ) {
        log.info("Received gRPC request: customerId={}",
                request.getCustomerId());

        UUID customerId;

        try {
            customerId = UUID.fromString(request.getCustomerId());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid customer ID: {}", request.getCustomerId());

            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Invalid customer ID")
                            .asRuntimeException()
            );
            return;
        }

        getCustomerByIdUseCase.execute(customerId)
                .subscribe(
                        customer -> {
                            log.info(
                                    "Customer found: customerId={}, username={}",
                                    customer.getId().value(),
                                    customer.getUsername()
                            );

                            GetCustomerByIdResponse response = mapToResponse(customer);

                            responseObserver.onNext(response);
                            responseObserver.onCompleted();

                            log.info("gRPC response sent successfully: customerId={}",
                                    customer.getId().value());
                        },
                        error -> {
                            log.error("Failed to retrieve customer: {}",
                                    error.getMessage());
                            responseObserver.onError(
                                    Status.NOT_FOUND
                                            .withDescription(error.getMessage())
                                            .asRuntimeException()
                            );
                        }
                );
    }

    private GetCustomerByIdResponse mapToResponse(Customer customer) {
        return GetCustomerByIdResponse.newBuilder()
                .setCustomerId(customer.getId().value().toString())
                .setUsername(customer.getUsername())
                .setFamilyName(customer.getFamilyName())
                .setGivenName(customer.getGivenName())
                .setEmail(customer.getEmail().value())
                .setPhoneNumber(customer.getPhoneNumber().value())
                .setStatus(customer.getStatus().name())
                .setSegmentId(
                        customer.getSegmentId() == null
                                ? ""
                                : customer.getSegmentId().toString()
                )

                .build();

    }
}
