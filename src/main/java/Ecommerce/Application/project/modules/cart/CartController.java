package Ecommerce.Application.project.modules.cart;

import Ecommerce.Application.project.modules.cart.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItem(
            @RequestAttribute("email") String email,
            @RequestBody AddToCartRequest request
    ) {
        return ResponseEntity.ok(cartService.addToCart(email, request));
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateItem(
            @RequestAttribute("email") String email,
            @RequestBody UpdateCartRequest request
    ) {
        return ResponseEntity.ok(cartService.updateItem(email, request));
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @RequestAttribute("email") String email,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(cartService.removeItem(email, itemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartResponse> clearCart(
            @RequestAttribute("email") String email
    ) {
        return ResponseEntity.ok(cartService.clearCart(email));
    }

    @GetMapping("")
    public ResponseEntity<CartResponse> getCart(
            @RequestAttribute("email") String email
    ) {
        return ResponseEntity.ok(cartService.getCart(email));
    }
}
