package org.tasks.myshop.service.mapper;

import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.dao.model.ItemPicsEntity;
import org.tasks.myshop.dto.ItemDto;
import org.tasks.myshop.dto.ItemPicsDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ItemMapperImplForTest implements ItemMapper {

    private ItemPicsMapper itemPicsMapper = new ItemPicsMapperImplForTest();

    @Override
    public ItemDto toDto(ItemEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ItemDto itemDto = new ItemDto();

        itemDto.setId( entity.getId() );
        itemDto.setTitle( entity.getTitle() );
        itemDto.setDescription( entity.getDescription() );
        itemDto.setPrice( entity.getPrice() );
        itemDto.setQuantity( entity.getQuantity() );
        itemDto.setItemPics( itemPicsMapper.toDto( entity.getItemPics() ) );

        afterMapping( itemDto );

        return itemDto;
    }

    @Override
    public List<ItemDto> toDto(List<ItemEntity> entity) {
        if ( entity == null ) {
            return null;
        }

        List<ItemDto> list = new ArrayList<ItemDto>( entity.size() );
        for ( ItemEntity itemEntity : entity ) {
            list.add( toDto( itemEntity ) );
        }

        return list;
    }

    @Override
    public ItemEntity toEntity(ItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        ItemEntity.ItemEntityBuilder itemEntity = ItemEntity.builder();

        itemEntity.id( dto.getId() );
        itemEntity.title( dto.getTitle() );
        itemEntity.description( dto.getDescription() );
        itemEntity.price( dto.getPrice() );
        itemEntity.quantity( dto.getQuantity() );
        itemEntity.itemPics( itemPicsDtoToItemPicsEntity( dto.getItemPics() ) );

        return itemEntity.build();
    }

    @Override
    public List<ItemEntity> toEntity(List<ItemDto> dto) {
        if ( dto == null ) {
            return null;
        }

        List<ItemEntity> list = new ArrayList<ItemEntity>( dto.size() );
        for ( ItemDto itemDto : dto ) {
            list.add( toEntity( itemDto ) );
        }

        return list;
    }

    protected ItemPicsEntity itemPicsDtoToItemPicsEntity(ItemPicsDto itemPicsDto) {
        if ( itemPicsDto == null ) {
            return null;
        }

        ItemPicsEntity itemPicsEntity = new ItemPicsEntity();

        itemPicsEntity.setItemId( itemPicsDto.getItemId() );
        itemPicsEntity.setImageType( itemPicsDto.getImageType() );
        byte[] image = itemPicsDto.getImage();
        if ( image != null ) {
            itemPicsEntity.setImage( Arrays.copyOf( image, image.length ) );
        }

        return itemPicsEntity;
    }
}
