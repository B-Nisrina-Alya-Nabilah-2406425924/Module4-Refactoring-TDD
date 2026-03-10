package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
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
    private static final String METHOD_VOUCHER = "VOUCHER";
    private static final String METHOD_VOUCHER_CODE = "VOUCHER_CODE";
    private static final String METHOD_BANK_TRANSFER = "BANK_TRANSFER";
    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_REJECTED = "REJECTED";

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(UUID.randomUUID().toString(), method, paymentData);
        payment.setOrder(order);

        String status = resolveInitialStatus(method, paymentData);
        return setStatus(payment, status);
    }

    private String resolveInitialStatus(String method, Map<String, String> paymentData) {
        boolean isValid;
        if (isVoucherMethod(method)) {
            isValid = validateVoucher(paymentData);
        } else if (METHOD_BANK_TRANSFER.equals(method)) {
            isValid = validateBankTransfer(paymentData);
        } else {
            isValid = false;
        }
        return isValid ? STATUS_SUCCESS : STATUS_REJECTED;
    }

    private boolean isVoucherMethod(String method) {
        return METHOD_VOUCHER.equals(method) || METHOD_VOUCHER_CODE.equals(method);
    }

    private boolean validateVoucher(Map<String, String> paymentData) {
        String voucher = paymentData.get("voucherCode");
        return voucher != null
                && voucher.length() == 16
                && voucher.startsWith("ESHOP")
                && voucher.replaceAll("[^0-9]", "").length() == 8;
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
            syncOrderStatus(payment.getOrder(), status);
        }

        paymentRepository.save(payment);
        return payment;
    }

    private void syncOrderStatus(Order order, String paymentStatus) {
        if (STATUS_SUCCESS.equals(paymentStatus)) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        } else if (STATUS_REJECTED.equals(paymentStatus)) {
            order.setStatus(OrderStatus.FAILED.getValue());
        }
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
