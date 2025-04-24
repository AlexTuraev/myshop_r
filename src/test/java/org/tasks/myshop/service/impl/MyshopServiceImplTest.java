package org.tasks.myshop.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.tasks.myshop.config.TestMapperConfig;
import org.tasks.myshop.dao.model.ItemEntity;
import org.tasks.myshop.dao.repository.ItemPicsRepository;
import org.tasks.myshop.dao.repository.ItemRespository;
import org.tasks.myshop.dto.ItemDto;
import org.tasks.myshop.service.CartService;
import org.tasks.myshop.service.MyshopService;
import org.tasks.myshop.service.mapper.ItemMapper;
import org.tasks.myshop.service.mapper.ItemModelMapper;
import org.tasks.myshop.service.mapper.ItemPicsMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MyshopServiceImpl.class)
@Import(TestMapperConfig.class)
class MyshopServiceImplTest {

    @Autowired
    private MyshopService myshopService;

    @MockitoBean
    private ItemRespository itemRespository;

    @MockitoBean
    private ItemPicsRepository itemPicsRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemPicsMapper itemPicsMapper;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private ItemModelMapper itemModelMapper;

    @Test
    void getItemById() {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setPrice(BigDecimal.valueOf(5));
        itemEntity.setDescription("description");
        itemEntity.setTitle("title");
        itemEntity.setQuantity(50);

        ItemDto expected = new ItemDto();
        expected.setId(itemEntity.getId());
        expected.setPrice(itemEntity.getPrice());
        expected.setDescription(itemEntity.getDescription());
        expected.setTitle(itemEntity.getTitle());
        expected.setQuantity(itemEntity.getQuantity());


        when(itemRespository.findById(1L))
                .thenReturn(Optional.of(itemEntity));

        ItemDto actual = myshopService.getItemById(1L);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getQuantity(), actual.getQuantity());

        when(itemRespository.findById(50L))
                .thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> myshopService.getItemById(50L));
    }

}