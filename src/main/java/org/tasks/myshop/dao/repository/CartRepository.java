package org.tasks.myshop.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tasks.myshop.dao.model.CartEntity;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByItemIdAndCartId(Long itemId, Long cartId);

    void deleteByItemIdAndCartId(Long itemId, Long cartId);

    @Query("""
        SELECT ct FROM CartEntity ct 
            JOIN FETCH ct.item 
                LEFT JOIN FETCH ct.item.itemPics WHERE ct.cartId=:cartId
    """)
    List<CartEntity> getCartModelByCartId(Long cartId);

}
