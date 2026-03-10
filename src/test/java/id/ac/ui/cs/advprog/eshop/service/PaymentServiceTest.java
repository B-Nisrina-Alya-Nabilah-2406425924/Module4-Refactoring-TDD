package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
    }

    @Test
    void testCreatePaymentVoucherCode() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("1", "VOUCHER", paymentData);

        assertEquals("1", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentBankTransfer() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF12345");
        Payment payment = new Payment("2", "BANK_TRANSFER", paymentData);

        assertEquals("2", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals(paymentData, payment.getPaymentData());
    }
}