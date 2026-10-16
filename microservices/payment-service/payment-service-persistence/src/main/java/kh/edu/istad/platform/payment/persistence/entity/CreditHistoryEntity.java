package kh.edu.istad.platform.payment.persistence.entity;

import jakarta.persistence.*;
import kh.edu.istad.platform.payment.domain.valueobject.TransactionType;
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
@Table(name = "credit_histories")
public class CreditHistoryEntity {

    @Id
    @Column(nullable = false)
    private UUID creditHistoryId;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;
}
