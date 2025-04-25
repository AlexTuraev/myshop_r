package org.tasks.myshop.dao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@Builder
@Table(name = "items")
public class ItemEntity {

    @Column("id")
    private Long id;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("price")
    private BigDecimal price;

    @Column("quantity")
    private Integer quantity;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", referencedColumnName = "item_id")
    private ItemPicsEntity itemPics;

    @Transient
    private Integer countInSomeCart;

}
