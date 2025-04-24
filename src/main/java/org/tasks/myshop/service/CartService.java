package org.tasks.myshop.service;

import org.springframework.ui.Model;
import org.tasks.myshop.dao.model.CartEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CartService {

    Optional<CartEntity> getCartByItemIdAndCartId(Long itemId, Long cartId);

    CartEntity updateCountItem(Long itemId, Long cartId, int deltaCount);

    public List<CartEntity> getCartsByCartId(Long cartId);

    Model getModelByCartId(Model model, Long cartId);

    int getCountItemOrZeroIfAbsent(Long itemId, Long cartId);

    void deleteAll(List<CartEntity> carts);

    BigDecimal getTotalSum(List<CartEntity> carts);

}
