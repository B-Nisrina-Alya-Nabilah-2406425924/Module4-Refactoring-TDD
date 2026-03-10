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

        boolean isValid = false;
        if (method.equals("VOUCHER")) {
            String voucher = paymentData.get("voucherCode");
            if (voucher != null && voucher.length() == 16 && voucher.startsWith("ESHOP")) {
                int numCount = 0;
                for (char c : voucher.toCharArray()) {
                    if (Character.isDigit(c)) numCount++;
                }
                if (numCount == 8) isValid = true;
            }
        } else if (method.equals("BANK_TRANSFER")) {
            String bank = paymentData.get("bankName");
            String ref = paymentData.get("referenceCode");
            if (bank != null && !bank.isEmpty() && ref != null && !ref.isEmpty()) {
                isValid = true;
            }
        }

        payment.setStatus(isValid ? "SUCCESS" : "REJECTED");
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
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