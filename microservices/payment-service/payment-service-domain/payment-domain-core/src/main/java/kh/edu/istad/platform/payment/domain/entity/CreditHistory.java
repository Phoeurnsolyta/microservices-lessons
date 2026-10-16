package kh.edu.istad.platform.payment.domain.entity;

import kh.edu.istad.platform.payment.domain.valueobject.CreditHistoryId;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.domain.valueobject.Money;
import kh.edu.istad.platform.payment.domain.valueobject.TransactionType;

import java.util.UUID;

public class CreditHistory {

    private final CreditHistoryId creditHistoryId;
    private final CustomerId customerId;
    private final Money amount;
    private final TransactionType transactionType;

    private CreditHistory(
            CreditHistoryId creditHistoryId,
            CustomerId customerId,
            Money amount,
            TransactionType transactionType
    ) {
        this.creditHistoryId = creditHistoryId;
        this.customerId = customerId;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public static CreditHistory create(
            CustomerId customerId,
            Money amount,
            TransactionType transactionType
    ) {

        return new CreditHistory(
                new CreditHistoryId(UUID.randomUUID()),
                customerId,
                amount,
                transactionType
        );
    }

    public CreditHistoryId getCreditHistoryId() {
        return creditHistoryId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
