package Ecommerce.Application.project.modules.cart.controller;

import Ecommerce.Application.project.modules.cart.dto.*;
import Ecommerce.Application.project.modules.cart.dto.cart_item.AddToCartRequest;
import Ecommerce.Application.project.modules.cart.dto.cart_item.UpdateCartRequest;
import Ecommerce.Application.project.modules.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart") // <── IMPORTANT: DO NOT PUT /api HERE
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // -----------------------------------------------------
    // GET CART
    // -----------------------------------------------------
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestAttribute("email") String email
    ) {
        return ResponseEntity.ok(cartService.getCart(email));
    }

    // -----------------------------------------------------
    // ADD TO CART
    // -----------------------------------------------------
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItem(
            @RequestAttribute("email") String email,
            @RequestBody AddToCartRequest request
    ) {
        return ResponseEntity.ok(cartService.addToCart(email, request));
    }

    // -----------------------------------------------------
    // UPDATE CART ITEM
    // -----------------------------------------------------
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateItem(
            @RequestAttribute("email") String email,
            @RequestBody UpdateCartRequest request
    ) {
        return ResponseEntity.ok(cartService.updateItem(email, request));
    }

    // -----------------------------------------------------
    // REMOVE ITEM
    // -----------------------------------------------------
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<CartResponse> removeItem(
            @RequestAttribute("email") String email,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(cartService.removeItem(email, itemId));
    }

    // -----------------------------------------------------
    // CLEAR CART
    // -----------------------------------------------------
    @DeleteMapping("/clear")
    public ResponseEntity<CartResponse> clearCart(
            @RequestAttribute("email") String email
    ) {
        return ResponseEntity.ok(cartService.clearCart(email));
    }
    // INCREASE QUANTITY
    @PutMapping("/increase/{itemId}")
    public ResponseEntity<CartResponse> increaseItem(
            @RequestAttribute("email") String email,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(cartService.increaseItem(email, itemId));
    }

    // DECREASE QUANTITY
    @PutMapping("/decrease/{itemId}")
    public ResponseEntity<CartResponse> decreaseItem(
            @RequestAttribute("email") String email,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(cartService.decreaseItem(email, itemId));
    }

}
