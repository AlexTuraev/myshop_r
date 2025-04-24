package org.tasks.myshop.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tasks.myshop.dao.model.complexid.CartEntityId;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "cart", uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "item_id"}))
@IdClass(CartEntityId.class)
public class CartEntity {

    @Id
    @Column(name = "cart_id")
    private Long cartId;

    @Id
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "count_item")
    private Integer countItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable=false, updatable=false)
    private ItemEntity item;

}
