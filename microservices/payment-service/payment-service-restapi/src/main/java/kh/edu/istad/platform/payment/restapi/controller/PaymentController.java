package kh.edu.istad.platform.payment.restapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    public record PaymentRequest(
            @NotNull UUID orderId,
            @NotNull @Positive BigDecimal amount
    ) {}

    public record PaymentResponse(
            UUID paymentId,
            UUID orderId,
            BigDecimal amount,
            String status
    ) {}

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = new PaymentResponse(
                UUID.randomUUID(),
                request.orderId(),
                request.amount(),
                "COMPLETED"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}