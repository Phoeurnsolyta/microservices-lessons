package kh.edu.istad.platform.iam.restapi.exception;

import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.exception.InactiveAssignmentException;
import kh.edu.istad.platform.iam.domain.exception.LastAuthenticationMethodRemovalException;
import kh.edu.istad.platform.iam.domain.exception.SystemResourceImmutableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String PROBLEM_BASE_URL = "http://iam.service/problems/";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed"
        );

        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "validation-error"));
        problemDetail.setTitle("Validation failed");

        List<Map<String, String>> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(Map.of(
                    "field", fieldError.getField(),
                    "message", fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value"
            ));
        }
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    @ExceptionHandler(SystemResourceImmutableException.class)
    public ProblemDetail handleSystemResourceImmutable(SystemResourceImmutableException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, ex.getMessage()
        );

        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "system-resource-immutable"));
        problemDetail.setTitle("System resource immutable");

        return problemDetail;
    }

    @ExceptionHandler(InactiveAssignmentException.class)
    public ProblemDetail handleInactiveAssignment(InactiveAssignmentException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage()
        );

        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "inactive-assignment"));

        problemDetail.setTitle("Inactive assignment");
        return problemDetail;
    }

    @ExceptionHandler(LastAuthenticationMethodRemovalException.class)
    public ProblemDetail handleLastAuthRemoval(LastAuthenticationMethodRemovalException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage()
        );

        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "last-auth-method-removal"));
        problemDetail.setTitle("Cannot remove last authentication method");
        return problemDetail;
    }

    @ExceptionHandler(IamDomainException.class)
    public ProblemDetail handleDomainException(IamDomainException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "domain-error")
        );

        problemDetail.setTitle("Domain rule violation");
        return problemDetail;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail handleNotFound(NoSuchElementException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage() != null ? ex.getMessage() : "Resource not found"
        );
        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "resource-not-found"));
        problemDetail.setTitle("Resource not found");

        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "You do not have permission to access this resource"
        );

        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "access-denied"));
        problemDetail.setTitle("Access denied");
        return problemDetail;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Invalid credentials"
        );

        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "unauthorized"));
        problemDetail.setTitle("Unauthorized");

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred"
        );

        problemDetail.setType(URI.create(PROBLEM_BASE_URL + "internal-server-error"));
        problemDetail.setTitle("Internal server error");

        return problemDetail;
    }
}
