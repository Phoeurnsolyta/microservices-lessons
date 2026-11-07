package kh.edu.istad.platform.iam.domain.exception;

public class SystemResourceImmutableException extends IamDomainException {

    public SystemResourceImmutableException(String resourceType, String name) {
        super(
                String.format("System %s '%s' is protected and cannot be modified or deleted", resourceType, name)
        );
    }
}
