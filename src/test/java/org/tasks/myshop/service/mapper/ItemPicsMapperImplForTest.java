package org.tasks.myshop.service.mapper;

import org.tasks.myshop.dao.model.ItemPicsEntity;
import org.tasks.myshop.dto.ItemPicsDto;

import java.util.Arrays;

public class ItemPicsMapperImplForTest implements ItemPicsMapper {

    @Override
    public ItemPicsDto toDto(ItemPicsEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ItemPicsDto itemPicsDto = new ItemPicsDto();

        itemPicsDto.setItemId( entity.getItemId() );
        itemPicsDto.setImageType( entity.getImageType() );
        byte[] image = entity.getImage();
        if ( image != null ) {
            itemPicsDto.setImage( Arrays.copyOf( image, image.length ) );
        }

        return itemPicsDto;
    }
}
