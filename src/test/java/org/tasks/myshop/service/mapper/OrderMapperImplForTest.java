package org.tasks.myshop.service.mapper;

import org.tasks.myshop.dao.model.OrderEntity;
import org.tasks.myshop.dto.OrderDto;

public class OrderMapperImplForTest implements OrderMapper {

    private ItemMapper itemMapper = new ItemMapperImplForTest();

    @Override
    public OrderDto toDto(OrderEntity order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setOrderId( order.getOrderId() );
        orderDto.setItemId( order.getItemId() );
        orderDto.setCountItem( order.getCountItem() );
        orderDto.setPrice( order.getPrice() );
        orderDto.setItem( itemMapper.toDto( order.getItem() ) );

        return orderDto;
    }
}

