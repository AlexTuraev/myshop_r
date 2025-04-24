package org.tasks.myshop.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tasks.myshop.dao.model.OrderEntity;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(value = "SELECT nextval('order_sequence')")
    Long getNextOrderId();

    @Query(value = """
        SELECT o.order_id as orderId,
               o.count_item as countItem,
               o.price as price,
               o.item_id as itemId,
               it.title as title,
               it.description as description
            FROM orders o
            LEFT JOIN items it ON it.id = o.item_id
    """,
            nativeQuery = true)
    List<?> findModelOrders();

    @Query("""
        SELECT o FROM OrderEntity o
            LEFT JOIN FETCH o.item
                LEFT JOIN FETCH o.item.itemPics WHERE o.orderId=:orderId
    """)
    List<OrderEntity> findAllWhereId(Long orderId);
}
