package pl.kajteh.payment;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.kajteh.payment.data.CashBillAmountData;
import pl.kajteh.payment.data.CashBillPersonalData;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class CashBillPayment {
    @NonNull
    private final String title;
    @NonNull private final CashBillAmountData amount;
    private String description;
    private String additionalData;
    private String returnUrl;
    private String negativeReturnUrl;
    private String paymentChannel;
    private String languageCode = "pl";
    private CashBillPersonalData personalData;
    private String referer;
    private Map<String, String> options;
}
