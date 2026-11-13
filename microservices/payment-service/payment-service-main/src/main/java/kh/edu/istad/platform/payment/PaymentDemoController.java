package kh.edu.istad.platform.payment;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class PaymentDemoController {

    @GetMapping("/jwt")
    public Map<String, Object> demo(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return Map.of(
                "message",
                "Order Service",
                "jwt payment service's token", jwt.getTokenValue()
        );
    }
}
