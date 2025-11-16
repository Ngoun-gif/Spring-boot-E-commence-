package Ecommerce.Application.project.modules.payment;

import Ecommerce.Application.project.modules.payment.dto.PaymentRequest;
import Ecommerce.Application.project.modules.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse createPayment(@RequestBody PaymentRequest req, Principal principal) {
        return paymentService.pay(req, principal.getName());
    }
}
