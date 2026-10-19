package kh.edu.istad.platform.customer.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document (collection = "customerSegmentDocument")
public class CustomerSegmentDocument {

    @Id
    private UUID id;

    private String name;
    private String description;
}
