package org.tasks.myshop.service.facade.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.tasks.myshop.dao.model.CartEntity;
import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.MyshopService;
import org.tasks.myshop.service.facade.CartChangeFcdService;

@Service
public class CartChangeFcdServiceImpl implements CartChangeFcdService {

    private final CartService cartService;
    private final MyshopService myshopService;

    public CartChangeFcdServiceImpl(CartService cartService, MyshopService myshopService) {
        this.cartService = cartService;
        this.myshopService = myshopService;
    }

    @Override
    @Transactional
    public void updateItemInCart(Long cartId, Long itemId, String action) {
        int delta = getDelta(cartId, itemId, action);

        CartEntity cart = cartService.updateCountItem(itemId, cartId, delta);
        ItemEntity item = myshopService.updateCountItem(itemId, -delta);
    }

    @Override
    public int getDelta(Long cartId, Long itemId, String action) {
        CartEntity cart = cartService.getCartByItemIdAndCartId(itemId, cartId)
                .orElse(new CartEntity(cartId, itemId, 0, null));

        return switch (action) {
            case "plus" -> 1;
            case "minus" -> -1;
            case "delete" -> -cart.getCountItem();
            default -> throw new IllegalArgumentException("Invalid action:  " + action);
        };
    }


}
