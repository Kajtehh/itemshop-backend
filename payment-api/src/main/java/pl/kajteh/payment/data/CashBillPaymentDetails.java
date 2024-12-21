package pl.kajteh.payment.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CashBillPaymentDetails {
    private String id;
    private String paymentChannel;
    private CashBillAmountData amount;
    private CashBillAmountData requestedAmount;
    private String title;
    private String description;
    private CashBillPersonalData personalData;
    private String additionalData;
    private String status;
}
