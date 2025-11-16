package Ecommerce.Application.project.modules.order;

import Ecommerce.Application.project.modules.order.dto.CheckoutRequest;
import Ecommerce.Application.project.modules.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    // --------------------------
    // CHECKOUT
    // --------------------------
    @PostMapping("/checkout")
    public OrderResponse checkout(@RequestBody CheckoutRequest req, Principal principal) {

        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }

        String email = principal.getName();
        return orderService.checkout(email, req);
    }

    // --------------------------
    // GET MY ORDERS
    // --------------------------
    @GetMapping
    public List<OrderResponse> getMyOrders(Principal principal) {

        if (principal == null) {
            throw new RuntimeException("Unauthorized");
        }

        String email = principal.getName();
        return orderService.getMyOrders(email);
    }
}
