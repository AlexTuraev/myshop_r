package org.tasks.myshop.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.dao.model.ItemModel;

@Repository
public interface ItemRespository extends JpaRepository<ItemEntity, Long> {
    @Query("""
        SELECT item FROM ItemEntity item LEFT JOIN FETCH item.itemPics WHERE item.title LIKE :search%
    """)
    Page<ItemEntity> findByTitle(String search, Pageable pageable);

    @Query("""
        SELECT item FROM ItemEntity item LEFT JOIN FETCH item.itemPics WHERE item.title LIKE :search% AND item.quantity >= :minQuantity
    """)
    Page<ItemEntity> findByTitleAndOverMinQuantity(String search, Pageable pageable, int minQuantity);

    @Query("""
        SELECT new org.tasks.myshop.dao.model.ItemModel(item, coalesce(c.countItem, 0) )
            FROM ItemEntity item
            LEFT JOIN CartEntity c ON c.itemId = item.id
            LEFT JOIN FETCH item.itemPics
                WHERE item.title LIKE :search% AND item.quantity >= :minQuantity
                    AND (c.countItem IS NULL OR c.cartId = 1)
    """)
    Page<ItemModel> findByTitleAndOverMinQuantityNew(String search, Pageable pageable, int minQuantity);
}
