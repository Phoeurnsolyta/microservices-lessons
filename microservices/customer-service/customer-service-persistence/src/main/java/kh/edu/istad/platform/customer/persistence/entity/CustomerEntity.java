package kh.edu.istad.platform.customer.persistence.entity;

import jakarta.persistence.*;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table (name = "customers")
public class CustomerEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String username;
    private String familyName;
    private String givenName;
    private String email;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;
}
