package kh.edu.istad.platform.order.restapi.controller;


import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.usecase.CreateOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<CreateOrderResult> createOrder(
            @RequestBody CreateOrderCommand command
    ) {
        String customerUrl = "http://CUSTOMER-SERVICE/api/customers/" + command.customerId();
        log.info("[Eureka Discovery] Calling Customer Service at: {}", customerUrl);
        Object customerResponse = restTemplate.getForObject(customerUrl, Object.class);
        log.info("[Eureka Discovery] Customer response: {}", customerResponse);

        CreateOrderResult result = createOrderUseCase.execute(command);

        String paymentUrl = "http://PAYMENT-SERVICE/api/payments";
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("orderId", result.trackingId());
        paymentRequest.put("customerId", command.customerId());

        log.info("[Eureka Discovery] Calling Payment Service at: {}", paymentUrl);
        Object paymentResponse = restTemplate.postForObject(paymentUrl, paymentRequest, Object.class);
        log.info("[Eureka Discovery] Payment response: {}", paymentResponse);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }
}
