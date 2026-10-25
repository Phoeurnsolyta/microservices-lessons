package kh.edu.istad.platform.order.restapi.controller;

import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.usecase.CreateOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
//    private final RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity<CreateOrderResult> createOrder(
            @RequestBody CreateOrderCommand command
    ) {
//        String customerUrl = "http://localhost:8081/api/customers/" + command.customerId();
//        log.info("Calling Customer Service at: {}", customerUrl);
//        Object customerResponse = restTemplate.getForObject(customerUrl, Object.class);
//        log.info("Received customer response: {}", customerResponse);

        CreateOrderResult result = createOrderUseCase.execute(command);

//        String paymentUrl = "http://localhost:8084/api/payments";
//        Map<String, Object> paymentRequest = new HashMap<>();
//        paymentRequest.put("orderId", result.trackingId());
//        paymentRequest.put("customerId", command.customerId());
//
//        log.info("Calling Payment Service at: {}", paymentUrl);
//        Object paymentResponse = restTemplate.postForObject(paymentUrl, paymentRequest, Object.class);
//        log.info("Received payment response: {}", paymentResponse);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }
}
