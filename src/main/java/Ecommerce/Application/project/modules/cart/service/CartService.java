package Ecommerce.Application.project.modules.cart.service;

import Ecommerce.Application.project.modules.cart.dto.*;
import Ecommerce.Application.project.modules.cart.dto.cart_item.AddToCartRequest;
import Ecommerce.Application.project.modules.cart.dto.cart_item.UpdateCartRequest;

public interface CartService {

    CartResponse getCart(String email);

    CartResponse addToCart(String email, AddToCartRequest request);

    CartResponse updateItem(String email, UpdateCartRequest request);

    CartResponse removeItem(String email, Long itemId);

    CartResponse clearCart(String email);

    CartResponse increaseItem(String email, Long itemId);

    CartResponse decreaseItem(String email, Long itemId);

}
