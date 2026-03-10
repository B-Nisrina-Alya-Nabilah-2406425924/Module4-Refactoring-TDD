package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    @Setter
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        if (paymentData == null || paymentData.isEmpty()) {
            throw new IllegalArgumentException("Payment data cannot be empty");
        }
    }
}