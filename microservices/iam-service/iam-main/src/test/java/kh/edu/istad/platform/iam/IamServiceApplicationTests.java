package kh.edu.istad.platform.iam;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Integration test requires running PostgreSQL container and OAuth2 environment variables")
class IamServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}