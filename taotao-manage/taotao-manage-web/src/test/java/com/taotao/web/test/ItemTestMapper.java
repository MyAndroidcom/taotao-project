package com.taotao.web.test;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;


public class ItemTestMapper {
    
    private ItemMapper itemMapper;
    @Before
    public void setUp(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "classpath:spring/applicationContext*.xml");
        this.itemMapper=applicationContext.getBean(ItemMapper.class);
    }
    @Test
    public void testItem(){
        Item item = new Item();
        item.setBarcode("qweqw");
        item.setStatus(123);
        item.setPrice(12L);
        item.setNum(12);
        item.setCid(12L);
        item.setTitle("魔力");
        item.setCreated(new Date());
        item.setUpdated(new Date());
        this.itemMapper.insertSelective(item);
    }
}
