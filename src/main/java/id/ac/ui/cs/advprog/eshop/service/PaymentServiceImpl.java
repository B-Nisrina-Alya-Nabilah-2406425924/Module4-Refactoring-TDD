package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(UUID.randomUUID().toString(), method, paymentData);
        payment.setOrder(order);

        boolean isValid = method.equals("VOUCHER") ? validateVoucher(paymentData) : validateBankTransfer(paymentData);

        if (isValid) {
            payment.setStatus("SUCCESS");
            order.setStatus("SUCCESS");
        } else {
            payment.setStatus("REJECTED");
            order.setStatus("FAILED");
        }

        return paymentRepository.save(payment);
    }

    private boolean validateVoucher(Map<String, String> paymentData) {
        String voucher = paymentData.get("voucherCode");
        return voucher != null && voucher.length() == 16 &&
                voucher.startsWith("ESHOP") &&
                voucher.replaceAll("[^0-9]", "").length() == 8;
    }

    private boolean validateBankTransfer(Map<String, String> paymentData) {
        String bank = paymentData.get("bankName");
        String ref = paymentData.get("referenceCode");
        return bank != null && !bank.isEmpty() && ref != null && !ref.isEmpty();
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);

        if (payment.getOrder() != null) {
            if (status.equals("SUCCESS")) {
                payment.getOrder().setStatus("SUCCESS");
            } else if (status.equals("REJECTED")) {
                payment.getOrder().setStatus("FAILED");
            }
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}