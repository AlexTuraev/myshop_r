package org.tasks.myshop.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "item_pics")
public class ItemPicsEntity {

    @Id
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "image_type")
    private String imageType;

    @Column(name = "image")
    private byte[] image;

}
