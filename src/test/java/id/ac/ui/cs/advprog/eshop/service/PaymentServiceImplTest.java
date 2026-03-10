package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;

    @BeforeEach
    void setUp() {
        order = new Order("order-1", new ArrayList<>(), 12345678L, "Nisrina");
    }

    @Test
    void testAddPaymentVoucherValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("pay-1", "VOUCHER", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalidLength() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123"); // Kurang dari 16

        Payment payment = new Payment("pay-1", "VOUCHER", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentVoucherInvalidPrefix() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "KSHOP1234ABC5678");

        Payment payment = new Payment("pay-1", "VOUCHER", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentVoucherInvalidDigitCount() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12ABCDEFGHI");

        Payment payment = new Payment("pay-1", "VOUCHER", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "VOUCHER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "TRX12345");

        Payment payment = new Payment("pay-2", "BANK_TRANSFER", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferInvalidEmptyBank() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "TRX12345");

        Payment payment = new Payment("pay-2", "BANK_TRANSFER", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferInvalidNullRef() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");

        Payment payment = new Payment("pay-2", "BANK_TRANSFER", paymentData);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testSetStatusSuccessUpdatesOrder() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("pay-3", "VOUCHER", paymentData);
        payment.setStatus("PENDING");

        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "SUCCESS");
        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusRejectedUpdatesOrder() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("pay-4", "VOUCHER", paymentData);
        payment.setStatus("PENDING");

        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "REJECTED");
        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", order.getStatus());
    }
}