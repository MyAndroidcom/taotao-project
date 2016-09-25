package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.manage.pojo.ItemCat;
import com.taotao.manage.service.ItemCatService;

@RequestMapping("item/cat")
@Controller
public class ItemCatController {
    
   @Autowired
   private ItemCatService itemCatService;
   /*
    * 根据父亲节点查询列表
    * */
   @RequestMapping(method=RequestMethod.GET)
   public ResponseEntity<List<ItemCat>> queryItemCat(
           @RequestParam(value="id",defaultValue="0") Long parentId){
       try {
        List<ItemCat> itemCat = this.itemCatService.queryItemCat(parentId);
           if(itemCat == null || itemCat.isEmpty()){
               //404
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
           }
           return ResponseEntity.ok(itemCat);
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
   }
}
