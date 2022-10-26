package com.pokemartspringboot.views;

import com.pokemartspringboot.cart.Cart;
import com.pokemartspringboot.cart.CartService;
import com.pokemartspringboot.cartitem.CartItem;
import com.pokemartspringboot.cartitem.CartItemService;
import com.pokemartspringboot.product.ProductService;
import com.pokemartspringboot.user.User;
import com.pokemartspringboot.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/carts")
public class CartViewController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public String viewCartPage(Model model) {
        User user = (User)model.getAttribute("user");
//        model.addAttribute("user", user);
        model.addAttribute("carts", cartService.findByUserId(user.getId()));
        return "cart-page";
    }

    @GetMapping("/checkOut/{id}")
    public String checkOut(@PathVariable("id") Long id, Model model) {
        User user = (User)model.getAttribute("user");
        Cart cart = cartService.findById(id);
        cart.checkOut();
        cartService.save(cart);
        Cart newCart = new Cart(user.getId());
        cartService.save(newCart);
        return "redirect:/carts";
    }

    @GetMapping("/add/{id}")
    public String addItemQuantity(@PathVariable("id") Long id) {
        CartItem cartItem = cartItemService.findById(id);
        cartItem.setQuantity(cartItem.getQuantity()+1);
        cartItemService.save(cartItem);
        return "redirect:/carts";
    }

    @GetMapping("/subtract/{id}")
    public String subtractItemQuantity(@PathVariable("id") Long id) {
        CartItem cartItem = cartItemService.findById(id);
        cartItem.setQuantity(cartItem.getQuantity()-1);
        if (cartItem.getQuantity() == 0) {
            cartItemService.delete(id);
            return "redirect:/carts";
        }
        cartItemService.save(cartItem);
        return "redirect:/carts";
    }
}
