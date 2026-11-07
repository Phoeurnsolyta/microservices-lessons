package kh.edu.istad.platform.iam.domain.exception;

public class LastAuthenticationMethodRemovalException extends IamDomainException {
    public LastAuthenticationMethodRemovalException() {
        super("A user cannot unlink their last sign-in method");
    }
}
