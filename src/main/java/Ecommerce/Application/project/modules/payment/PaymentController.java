package Ecommerce.Application.project.modules.payment;

import Ecommerce.Application.project.modules.payment.dto.PaymentRequest;
import Ecommerce.Application.project.modules.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // ================== PAY ==================
    @PostMapping
    public ResponseEntity<PaymentResponse> pay(
            @RequestBody PaymentRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(
                paymentService.pay(request, principal.getName())
        );
    }

    // ================== MY LAST PAYMENT ==================
    @GetMapping("/me")
    public ResponseEntity<PaymentResponse> getMyLatestPayment(
            Principal principal
    ) {
        return ResponseEntity.ok(
                paymentService.getMyLatestPayment(principal.getName())
        );
    }

    // ================== PAYMENT HISTORY ==================
    @GetMapping("/list")
    public ResponseEntity<List<PaymentResponse>> getMyPayments(
            Principal principal
    ) {
        return ResponseEntity.ok(
                paymentService.getMyPayments(principal.getName())
        );
    }


}
