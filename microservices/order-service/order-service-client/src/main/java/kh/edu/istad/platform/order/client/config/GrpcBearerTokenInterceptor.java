package kh.edu.istad.platform.order.client.config;

import io.grpc.*;

public class GrpcBearerTokenInterceptor implements ClientInterceptor {

    private static final Metadata.Key<String> AUTHORIZATION =
            Metadata.Key.of(
                    "Authorization",
                    Metadata.ASCII_STRING_MARSHALLER
            );

    private final String accessToken;

    public GrpcBearerTokenInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next
    ) {

        return new ForwardingClientCall.SimpleForwardingClientCall<>(
                next.newCall(method, callOptions)
        ) {

            @Override
            public void start(
                    Listener<RespT> responseListener,
                    Metadata headers
            ) {
                headers.put(
                        AUTHORIZATION,
                        "Bearer " + accessToken
                );

                super.start(responseListener, headers);
            }
        };
    }
}
