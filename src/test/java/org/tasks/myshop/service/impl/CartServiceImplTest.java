package org.tasks.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.tasks.myshop.dao.model.CartEntity;
import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.dao.repository.CartRepository;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.mapper.CartMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CartServiceImpl.class)
class CartServiceImplTest {

    @Autowired
    private CartService cartService;

    @MockitoBean
    private CartRepository cartRepository;

    @MockitoBean
    private CartMapper cartMapper;

    @Test
    void updateCountItem() {
        when(cartService.getCartByItemIdAndCartId(1L, 1L))
                .thenReturn(Optional.of(new CartEntity(1L, 1L, 0, null)));
        assertThrows(RuntimeException.class, () -> cartService.updateCountItem(1L, 1L, -1));



        when(cartRepository.save(any(CartEntity.class))).thenAnswer(i -> i.getArgument(0));

        // +1
        when(cartService.getCartByItemIdAndCartId(2L, 1L))
                .thenReturn(Optional.of(new CartEntity(1L, 2L, 5, null)));

        CartEntity expected6 = new CartEntity(1L, 2L, 6, null);
        var actual = cartService.updateCountItem(2L, 1L, 1);
        assertEquals(actual, expected6);

        // -1
        when(cartService.getCartByItemIdAndCartId(3L, 1L))
                .thenReturn(Optional.of(new CartEntity(1L, 3L, 8, null)));
        CartEntity expected7 = new CartEntity(1L, 3L, 7, null);
        actual = cartService.updateCountItem(3L, 1L, -1);
        assertEquals(actual, expected7);

        // delete
        when(cartService.getCartByItemIdAndCartId(4L, 1L))
                .thenReturn(Optional.of(new CartEntity(1L, 4L, 10, null)));
        CartEntity expected0 = new CartEntity(1L, 4L, 0, null);
        actual = cartService.updateCountItem(4L, 1L, -10);
        assertEquals(actual, expected0);
    }

    @Test
    void getCartsByCartId() {
        List<CartEntity> expected = List.of(
                new CartEntity(1L, 1L, 1, null),
                new CartEntity(1L, 2L, 2, null),
                new CartEntity(1L, 3L, 5, null)
        );

        when(cartRepository.getCartModelByCartId(1L)).thenReturn(expected);

        List<CartEntity> actual = cartService.getCartsByCartId(1L);
        assertEquals(actual, expected);
    }

    @Test
    void getCountItemOrZeroIfAbsent() {
        when(cartRepository.findByItemIdAndCartId(1L, 1L))
                .thenReturn(Optional.of(new CartEntity(1L, 1L, 5, null)));
        int actual = cartService.getCountItemOrZeroIfAbsent(1L, 1L);
        assertEquals(5, actual);

        when(cartRepository.findByItemIdAndCartId(2L, 1L))
                .thenReturn(Optional.empty());
        actual = cartService.getCountItemOrZeroIfAbsent(2L, 1L);
        assertEquals(0, actual);
    }

    @Test
    void getTotalSum() {
        ItemEntity item1 = new ItemEntity();
            item1.setPrice(BigDecimal.valueOf(5));
        ItemEntity item2 = new ItemEntity();
            item2.setPrice(BigDecimal.valueOf(10));
        ItemEntity item3 = new ItemEntity();
            item3.setPrice(BigDecimal.valueOf(78));

        List<CartEntity> carts = List.of(
                new CartEntity(1L, 1L, 2, item1),
                new CartEntity(1L, 2L, 15, item2),
                new CartEntity(1L, 3L, 5, item3)
        );

        BigDecimal expected = BigDecimal.valueOf(2*5+15*10+5*78);
        BigDecimal actual = cartService.getTotalSum(carts);
        assertEquals(expected, actual);

    }
}