package kh.edu.istad.platform.business.persistence.document;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "businesses")
public class BusinessDocument {

    @Id
    private BusinessId id;

    private UUID businessId;
    private String legalName;
    private String displayName;
    private List<String> emails;
    private String phoneNumber;
    private boolean active;

    public BusinessDocument(
            UUID businessId,
            String legalName,
            String displayName,
            List<String> emails,
            String phoneNumber,
            boolean active
    ) {
        this.id = new BusinessId(businessId);
        this.businessId = businessId;
        this.legalName = legalName;
        this.displayName = displayName;
        this.emails = emails == null ? List.of() : List.copyOf(emails);
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

}
