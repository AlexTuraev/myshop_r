package org.tasks.myshop.service.facade.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.tasks.myshop.dao.model.CartEntity;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.MyshopService;
import org.tasks.myshop.service.facade.CartChangeFcdService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CartChangeFcdServiceImpl.class)
class CartChangeFcdServiceImplTest {

    @Autowired
    private CartChangeFcdService cartChangeFcdService;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private MyshopService myshopService;

    @Test
    void getDelta() {
        when(cartService.getCartByItemIdAndCartId(anyLong(), anyLong()))
                .thenReturn(Optional.of(new CartEntity(1L, 1L, 5, null)));

        assertEquals(-1, cartChangeFcdService.getDelta(1L, 1L, "minus"));
        assertEquals(1, cartChangeFcdService.getDelta(1L, 1L, "plus"));
        assertEquals(-5, cartChangeFcdService.getDelta(1L, 1L, "delete"));
    }
}