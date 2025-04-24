package org.tasks.myshop.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.tasks.myshop.config.TestMapperConfig;
import org.tasks.myshop.dao.model.OrderEntity;
import org.tasks.myshop.dao.repository.OrderRepository;
import org.tasks.myshop.dto.OrderDto;
import org.tasks.myshop.service.OrderService;
import org.tasks.myshop.service.mapper.ItemMapper;
import org.tasks.myshop.service.mapper.OrderMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = OrderServiceImpl.class)
@Import(TestMapperConfig.class)
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @MockitoBean
    private ItemMapper itemMapper;

    List<OrderEntity> orders;
    List<OrderDto> dtos;

    @BeforeEach
    void setUp() {
        OrderEntity order1 = new OrderEntity();
            order1.setOrderId(1L);
            order1.setItemId(1L);
            order1.setPrice(BigDecimal.valueOf(5));
            order1.setCountItem(2);

        OrderEntity order2 = new OrderEntity();
            order2.setOrderId(1L);
            order2.setItemId(2L);
            order2.setPrice(BigDecimal.valueOf(15));
            order2.setCountItem(3);

        OrderEntity order3 = new OrderEntity();
        order3.setOrderId(1L);
        order3.setItemId(3L);
        order3.setPrice(BigDecimal.valueOf(25));
        order3.setCountItem(10);

        this.orders = List.of(order1, order2, order3);

        OrderDto orderDto1 = new OrderDto();
            orderDto1.setOrderId(1L);
            orderDto1.setItemId(1L);
            orderDto1.setPrice(BigDecimal.valueOf(5));
            orderDto1.setCountItem(2);
        OrderDto orderDto2 = new OrderDto();
            orderDto2.setOrderId(1L);
            orderDto2.setItemId(2L);
            orderDto2.setPrice(BigDecimal.valueOf(15));
            orderDto2.setCountItem(3);
        OrderDto orderDto3 = new OrderDto();
            orderDto3.setOrderId(1L);
            orderDto3.setItemId(3L);
            orderDto3.setPrice(BigDecimal.valueOf(25));
            orderDto3.setCountItem(10);

        this.dtos = List.of(orderDto1, orderDto2, orderDto3);
    }

    @Test
    void saveAll() {
        when(orderRepository.saveAll(orders)).thenReturn(orders);
        List<OrderEntity> actual = orderService.saveAll(orders);
        assertEquals(orders, actual);
    }

    @Test
    void getOrdersById() {
        when(orderRepository.findAllWhereId(1L)).thenReturn(orders);
        List<OrderDto> actual = orderService.getOrdersById(1L);

        assertEquals(dtos, actual);

    }

}