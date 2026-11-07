package kh.edu.istad.platform.iam.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.usecase.user.RegisterUserUseCase;
import kh.edu.istad.platform.iam.restapi.dto.request.RegisterUserRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.UserResponse;
import kh.edu.istad.platform.iam.restapi.mapper.UserWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UserWebMapper userWebMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody RegisterUserRequest request
    ) {
        UserResult result = registerUserUseCase
                .execute(userWebMapper.toCommand(request));

        return userWebMapper.toResponse(result);
    }
}
