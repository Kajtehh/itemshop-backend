package pl.kajteh.payment.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CashBillPersonalData {
    private String firstName;
    private String surname;
    private String email;
    private String country;
    private String city;
    private String postcode;
    private String street;
    private String house;
    private String flat;
    private String ip;
}
