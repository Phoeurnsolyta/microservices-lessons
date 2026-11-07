package kh.edu.istad.platform.iam.domain.exception;

public class IamDomainException extends RuntimeException {
    public IamDomainException(String message) {
        super(message);
    }

    public IamDomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
