package org.tasks.myshop.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.tasks.myshop.service.mapper.ItemMapper;
import org.tasks.myshop.service.mapper.ItemMapperImplForTest;
import org.tasks.myshop.service.mapper.ItemPicsMapper;
import org.tasks.myshop.service.mapper.ItemPicsMapperImplForTest;
import org.tasks.myshop.service.mapper.OrderMapper;
import org.tasks.myshop.service.mapper.OrderMapperImplForTest;

@TestConfiguration
public class TestMapperConfig {

    @Bean
    public ItemMapper itemMapper() {
        return new ItemMapperImplForTest();
    }

    @Bean
    public ItemPicsMapper itemPicsMapper() {
        return new ItemPicsMapperImplForTest();
    }

    @Bean
    public OrderMapper orderMapper() {
        return new OrderMapperImplForTest();
    }

}
