package kh.edu.istad.platform.iam.infrastructure.adapter;

import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextCurrentActorProvider implements CurrentActorProvider {

    @Override
    public String getCurrentActorSub() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (
                auth == null || !auth.isAuthenticated() || "anonymousUser"
                        .equals(auth.getPrincipal())
        ) {
            return "system";
        }
        return auth.getName();
    }
}
