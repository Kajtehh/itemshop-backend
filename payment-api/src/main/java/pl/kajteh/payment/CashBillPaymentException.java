package pl.kajteh.payment;

import lombok.NonNull;

public class CashBillPaymentException extends Exception{

    public CashBillPaymentException(@NonNull String message) {
        super(message);
    }

    public CashBillPaymentException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }
}
