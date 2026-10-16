package kh.edu.istad.platform.payment.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "credit_entries")
public class CreditEntryEntity {

    @Id
    @Column (nullable = false)
    private UUID creditEntryId;

    @Column (nullable = false)
    private UUID customerId;

    @Column (nullable = false, precision = 19, scale = 2)
    private BigDecimal totalCreditAmount;

}
