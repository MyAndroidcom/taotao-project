package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.service.RedisService;
import com.taotao.web.service.ItemService;

/*
 * 后台系统中商品修改后向其他系统发送通知，其他系统做出对应的处理即可。
 *  1、  在前台系统中开发一个接口
 * */
@Controller
@RequestMapping("item/cache")
public class ItemCacheController {
    
    @Autowired
    private RedisService redisService;
    /*
     * 删除redis中商品的缓存数据
     * */
    @RequestMapping(value="{itemId}",method=RequestMethod.POST)
    public ResponseEntity<Void> deleteCache(@PathVariable("itemId") Long itemId){
        try{
            String key = ItemService.REDIS_KEY + itemId;
            this.redisService.del(key);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
