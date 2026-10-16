package kh.edu.istad.platform.payment.domain.entity;

import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.valueobject.CreditEntryId;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.domain.valueobject.Money;

import java.util.UUID;

public class CreditEntry {

    private final CreditEntryId creditEntryId;
    private final CustomerId customerId;
    private Money totalCreditAmount;

    private CreditEntry(
            CreditEntryId creditEntryId,
            CustomerId customerId,
            Money totalCreditAmount
    ) {
        this.creditEntryId = creditEntryId;
        this.customerId = customerId;
        this.totalCreditAmount = totalCreditAmount;
    }

    public static CreditEntry initializeCreditEntry(
            CustomerId customerId
    ) {
        if (customerId == null) {
            throw new PaymentDomainException("Customer ID is required");
        }
        return new CreditEntry(
                new CreditEntryId(UUID.randomUUID()),
                customerId,
                Money.zero()
        );
    }

    public void addCreditAmount(Money amount) {
        validateAmount(amount);
        totalCreditAmount = totalCreditAmount.add(amount);
    }

    public void subtractCreditAmount(Money amount) {
        validateAmount(amount);

        if (amount.isGreaterThan(totalCreditAmount)) {
            throw new PaymentDomainException(
                    "Insufficient credit balance"
            );
        }
        totalCreditAmount = totalCreditAmount.subtract(amount);
    }

    private void validateAmount(Money amount) {
        if (amount == null) {
            throw new PaymentDomainException(
                    "Credit amount is required"
            );
        }

        if (amount.amount().signum() <= 0) {
            throw new PaymentDomainException(
                    "Credit amount must be greater than zero"
            );
        }
    }

    public static CreditEntry restore(
            CreditEntryId creditEntryId,
            CustomerId customerId,
            Money totalCreditAmount
    ) {
        return new CreditEntry(
                creditEntryId,
                customerId,
                totalCreditAmount
        );
    }

    public CreditEntryId getCreditEntryId() {
        return creditEntryId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

}
