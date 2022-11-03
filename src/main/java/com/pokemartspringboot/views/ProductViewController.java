package com.pokemartspringboot.views;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@ControllerAdvice
@RequestMapping("/products")
public class ProductViewController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    @ModelAttribute("user")
    public User user() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUserName(auth.getName());
    }

    @GetMapping
    public String viewProductPage(Model model) {
        User user = (User)model.getAttribute("user");
        Cart activeCart = cartService.findByUserIdAndCheckedOut(user.getId(),false).get(0);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("cart", activeCart);
        return "product-page";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable("id") Long id, Model model) {
        User user = (User)model.getAttribute("user");
        Cart activeCart = cartService.findByUserIdAndCheckedOut(user.getId(),false).get(0);
//        model.addAttribute("products", productService.findAll());
        Collection<CartItem> cartItems = activeCart.getCartItems();
        for(CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getId().equals(id)) {
                cartItem.setQuantity(cartItem.getQuantity()+1);
                cartItemService.save(cartItem);
                return "redirect:/products";
            }
        }
        CartItem newCartItem = new CartItem(activeCart.getId(), 1, productService.findById(id));
        cartItemService.save(newCartItem);
        return "redirect:/products";
    }

}
