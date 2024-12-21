package pl.kajteh.payment;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;
import pl.kajteh.payment.data.CashBillPersonalData;

import java.util.Map;

@UtilityClass
public class CashBillPaymentUtil {

    public static String generatePaymentSignature(@NonNull CashBillPayment payment, @NonNull CashBillShop shop) {
        StringBuilder sb = new StringBuilder();

        appendNonNull(sb, payment.getTitle());
        appendNonNull(sb, payment.getAmount().value());
        appendNonNull(sb, payment.getAmount().currencyCode());
        appendNonNull(sb, payment.getReturnUrl());
        appendNonNull(sb, payment.getDescription());
        appendNonNull(sb, payment.getNegativeReturnUrl());
        appendNonNull(sb, payment.getAdditionalData());
        appendNonNull(sb, payment.getPaymentChannel());
        appendNonNull(sb, payment.getLanguageCode());
        appendNonNull(sb, payment.getReferer());

        if (payment.getPersonalData() != null) {
            final CashBillPersonalData personalData = payment.getPersonalData();

            appendNonNull(sb, personalData.getFirstName());
            appendNonNull(sb, personalData.getSurname());
            appendNonNull(sb, personalData.getEmail());
            appendNonNull(sb, personalData.getCountry());
            appendNonNull(sb, personalData.getCity());
            appendNonNull(sb, personalData.getPostcode());
            appendNonNull(sb, personalData.getStreet());
            appendNonNull(sb, personalData.getHouse());
            appendNonNull(sb, personalData.getFlat());
            appendNonNull(sb, personalData.getIp());
        }

        if (payment.getOptions() != null) {
            final StringBuilder mapBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : payment.getOptions().entrySet()) {
                appendNonNull(mapBuilder, entry.getKey());
                appendNonNull(mapBuilder, entry.getValue());
            }
            appendNonNull(sb, mapBuilder.toString());
        }

        sb.append(shop.secretKey);

        return DigestUtils.sha1Hex(sb.toString());
    }

    public static String getPaymentUrl(@NonNull CashBillShop shop) {
        return "https://pay.cashbill.pl/:ws/rest/payment/:shopId"
                .replace(":shopId", shop.shopId)
                .replace(":ws", shop.test ? "testws" : "ws");
    }

    public static String getTransactionInfoUrl(@NonNull CashBillShop shop, @NonNull String orderId) {
        return "https://pay.cashbill.pl/:ws/rest/payment/:shopId/:orderId?sign=:sign"
                .replace(":shopId", shop.shopId)
                .replace(":orderId", orderId)
                .replace(":ws", shop.test ? "testws" : "ws")
                .replace(":sign", DigestUtils.sha1Hex(orderId + shop.secretKey));
    }

    private static <T> void appendNonNull(StringBuilder sb, T value) {
        if (value != null) {
            sb.append(value);
        }
    }
}
