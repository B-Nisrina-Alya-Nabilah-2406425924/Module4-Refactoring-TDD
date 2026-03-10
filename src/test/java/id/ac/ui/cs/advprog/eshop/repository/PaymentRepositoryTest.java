package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testSaveAndFindById() {
        Payment payment = new Payment("1", "VOUCHER", paymentData);
        paymentRepository.save(payment);
        Payment saved = paymentRepository.findById("1");
        assertNotNull(saved);
        assertEquals("1", saved.getId());
    }

    @Test
    void testFindAll() {
        paymentRepository.save(new Payment("1", "VOUCHER", paymentData));
        paymentRepository.save(new Payment("2", "BANK_TRANSFER", paymentData));

        List<Payment> payments = paymentRepository.findAll();
        assertEquals(2, payments.size());
    }
}