package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "order/create";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "order/history";
    }

    @PostMapping("/history")
    public String historyOrderPost(@RequestParam String author, Model model) {
        model.addAttribute("orders", orderService.findAllByAuthor(author));
        return "order/historyList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        model.addAttribute("order", orderService.findById(orderId));
        return "order/pay";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable String orderId, Model model) {
        String dummyPaymentId = UUID.randomUUID().toString();
        model.addAttribute("paymentId", dummyPaymentId);
        return "order/paySuccess";
    }
}