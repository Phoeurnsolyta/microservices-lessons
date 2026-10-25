package kh.edu.istad.platform.payment.restapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Value("${server.port:8084}")
    private String serverPort;

    @PostMapping
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "paymentId", UUID.randomUUID().toString(),
                "orderId", request.getOrDefault("orderId", UUID.randomUUID().toString()),
                "amount", request.getOrDefault("amount", 0.0),
                "status", "COMPLETED",
                "servedByPort", serverPort
        ));
    }
}
