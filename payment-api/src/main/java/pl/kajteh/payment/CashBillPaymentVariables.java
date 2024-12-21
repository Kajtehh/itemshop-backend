package pl.kajteh.payment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.MediaType;

public class CashBillPaymentVariables {

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static final MediaType MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");
}
