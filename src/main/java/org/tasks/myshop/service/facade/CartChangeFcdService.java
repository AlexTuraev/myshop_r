package org.tasks.myshop.service.facade;

public interface CartChangeFcdService {

    int getDelta(Long cartId, Long itemId, String action);

    void updateItemInCart(Long cartId, Long itemId, String action);

}
