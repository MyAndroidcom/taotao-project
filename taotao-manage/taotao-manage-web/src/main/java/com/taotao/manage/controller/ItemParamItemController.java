package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemParamItemService;

@Controller
@RequestMapping("item/param/item")
public class ItemParamItemController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    
    
    @Autowired
    private ItemParamItemService itemParamItemService;
    /*
     * 根据商品id查询商品规格参数，用于在编辑页面回显规格参数数据
     * */
    @RequestMapping(value="{itemId}")
    public ResponseEntity<ItemParamItem> queryByItemId(
            @PathVariable("itemId") Long itemId){
        try{
            ItemParamItem record = new ItemParamItem();
            record.setItemId(itemId);
            ItemParamItem itemParamItem = this.itemParamItemService.queryOne(record);
            if(null==itemParamItem){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("查询商品规格成功:itemParamItem={},itemParamItemId={}",itemParamItem.getId(),itemParamItem.getItemId());
            }
            System.out.println("=================================="+itemParamItem.getId());
            return ResponseEntity.ok(itemParamItem);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    
}
