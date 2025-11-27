package Ecommerce.Application.project.modules.cart.controller;

import Ecommerce.Application.project.modules.cart.dto.*;
import Ecommerce.Application.project.modules.cart.dto.cart_item.AddToCartRequest;
import Ecommerce.Application.project.modules.cart.dto.cart_item.UpdateCartRequest;
import Ecommerce.Application.project.modules.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Helper method to get current user email from Security Context
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return authentication.getName();
    }

    // -----------------------------------------------------
    // GET CART
    // -----------------------------------------------------
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(cartService.getCart(email));
    }

    // -----------------------------------------------------
    // ADD TO CART
    // -----------------------------------------------------
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItem(@RequestBody AddToCartRequest request) {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(cartService.addToCart(email, request));
    }

    // -----------------------------------------------------
    // UPDATE CART ITEM
    // -----------------------------------------------------
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateItem(@RequestBody UpdateCartRequest request) {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(cartService.updateItem(email, request));
    }

    // -----------------------------------------------------
    // REMOVE ITEM
    // -----------------------------------------------------
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable Long itemId) {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(cartService.removeItem(email, itemId));
    }

    // -----------------------------------------------------
    // CLEAR CART
    // -----------------------------------------------------
    @DeleteMapping("/clear")
    public ResponseEntity<CartResponse> clearCart() {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(cartService.clearCart(email));
    }

    // -----------------------------------------------------
    // INCREASE QUANTITY
    // -----------------------------------------------------
    @PutMapping("/increase/{itemId}")
    public ResponseEntity<CartResponse> increaseItem(@PathVariable Long itemId) {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(cartService.increaseItem(email, itemId));
    }

    // -----------------------------------------------------
    // DECREASE QUANTITY
    // -----------------------------------------------------
    @PutMapping("/decrease/{itemId}")
    public ResponseEntity<CartResponse> decreaseItem(@PathVariable Long itemId) {
        String email = getCurrentUserEmail();
        return ResponseEntity.ok(cartService.decreaseItem(email, itemId));
    }
}