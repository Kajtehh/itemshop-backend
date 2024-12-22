package pl.kajteh.payment;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.kajteh.payment.data.CashBillGeneratedPayment;
import pl.kajteh.payment.data.CashBillPaymentDetails;

import java.io.IOException;
import java.util.Objects;

import static pl.kajteh.payment.CashBillPaymentVariables.GSON;
import static pl.kajteh.payment.CashBillPaymentVariables.MEDIA_TYPE;

@Getter
@RequiredArgsConstructor
public class CashBillShop {
    protected final String shopId;
    protected final String secretKey;
    @Setter protected boolean test = false;
    @Setter private OkHttpClient client = new OkHttpClient();

    /**
     * Creates a CashBillGeneratedPayment based on the provided CashBillPayment.
     * <p>
     * This method takes a CashBillPayment object as input and generates a CashBillGeneratedPayment
     * object based on the provided payment details. The generated payment is returned as a result.
     *
     * @param payment The CashBillPayment object containing payment details.
     * @return A CashBillGeneratedPayment object representing the generated payment.
     * @throws CashBillPaymentException if there's an issue with payment creation
     */
    public CashBillGeneratedPayment createPayment(@NonNull CashBillPayment payment) throws CashBillPaymentException {
        try {
            final JsonObject jsonObject = GSON.toJsonTree(payment).getAsJsonObject();

            jsonObject.addProperty("sign", CashBillPaymentUtil.generatePaymentSignature(payment, this));

            final String json = GSON.toJson(jsonObject);

            final RequestBody requestBody = RequestBody.create(json, MEDIA_TYPE);
            final Request request = new Request.Builder()
                    .url(CashBillPaymentUtil.getPaymentUrl(this))
                    .post(requestBody)
                    .build();

            final Response response = this.client.newCall(request).execute();

            final String body = Objects.requireNonNull(response.body()).string();

            if(!response.isSuccessful()) {
                throw new CashBillPaymentException("Payment creation failed with code: " + response.code() + " and body: " + body);
            }

            return GSON.fromJson(body, CashBillGeneratedPayment.class);
        } catch (IOException e) {
            throw new CashBillPaymentException("Payment creation failed", e);
        }
    }

    /**
     * Retrieves transaction information for the provided orderId.
     * <p>
     * This method sends a request to retrieve transaction information associated with the provided orderId.
     *
     * @param orderId The orderId for which transaction information is to be retrieved.
     * @return A CashBillPaymentDetails object containing transaction information.
     * @throws CashBillPaymentException if there's an issue retrieving transaction information
     */
    public CashBillPaymentDetails getPayment(@NonNull String orderId) throws CashBillPaymentException {
        try {
            final Request request = new Request.Builder()
                    .url(CashBillPaymentUtil.getTransactionInfoUrl(this, orderId))
                    .get()
                    .build();

            final Response response = this.client.newCall(request).execute();
            final String body = Objects.requireNonNull(response.body()).string();

            if(!response.isSuccessful()) {
                throw new CashBillPaymentException("Transaction info failed with code: " + response.code() + " and body: " + body);
            }

            return GSON.fromJson(body, CashBillPaymentDetails.class);
        } catch (IOException e) {
            throw new CashBillPaymentException("Failed to retrieve transaction info", e);
        }
    }
}
