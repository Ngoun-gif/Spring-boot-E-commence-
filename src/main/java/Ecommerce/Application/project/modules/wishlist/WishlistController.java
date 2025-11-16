package Ecommerce.Application.project.modules.wishlist;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

import Ecommerce.Application.project.modules.wishlist.dto.WishlistResponse;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add/{productId}")
    public String add(@PathVariable Long productId, Principal principal) {
        return wishlistService.addToWishlist(principal.getName(), productId);
    }

    @GetMapping
    public List<WishlistResponse> get(Principal principal) {
        return wishlistService.getWishlist(principal.getName());
    }

    @DeleteMapping("/remove/{productId}")
    public String remove(@PathVariable Long productId, Principal principal) {
        return wishlistService.remove(principal.getName(), productId);
    }
}
