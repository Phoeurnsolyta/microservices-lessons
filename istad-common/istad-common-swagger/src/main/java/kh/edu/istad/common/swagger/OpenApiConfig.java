package kh.edu.istad.common.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.AutoConfiguration;

@AutoConfiguration
@OpenAPIDefinition (
        info = @Info(
                title = "ISTAD ITE PLATFORM",
                description = "Luca Domenico"
        )
)
public class OpenApiConfig {
}
