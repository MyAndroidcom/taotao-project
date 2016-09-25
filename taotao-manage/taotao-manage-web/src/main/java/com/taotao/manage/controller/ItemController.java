package com.taotao.manage.controller;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);
    
    @Autowired 
    private ItemService itemService;
    
    /*
     * 新增商品
     * */
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item,
             String desc,String itemParams){
        try{
            if(StringUtils.isEmpty(item.getTitle())){
                //400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            //保存商品的基本数据
            this.itemService.saveItem(item,desc,itemParams);
            //保存成功 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(Exception e){
            e.printStackTrace();
        }
        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
   
    /*
     * 查询商品列表
     * */
    @RequestMapping(method=RequestMethod.GET)
    @ResponseBody
    public EasyUIResult queryItemList(
            @RequestParam(value="page",defaultValue="1") Integer page,
            @RequestParam(value="rows",defaultValue="30") Integer rows){
        return this.itemService.queryPageList(page, rows);
      /*  try{
            EasyUIResult easyui =  this.itemService.queryPageList(page, rows);
            return ResponseEntity.ok(easyui);
        }catch(Exception e){
            e.printStackTrace();
        }
        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);*/
    }
    /*
     * 编辑商品信息
     * */
    @RequestMapping(value="/edit",method=RequestMethod.PUT)
    public ResponseEntity<Void> editItem(Item item,@RequestParam("desc") String desc,
            String itemParams){
        try{
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("修改商品，item={},desc={}",item,desc);
            }
            if(StringUtils.isEmpty(item.getTitle())){
                //400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            this.itemService.editItem(item,desc,itemParams);
            // 成功 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch(Exception e){
            LOGGER.error("修改商品失败！title="+item.getTitle()+",cid="+item.getCid());
        }
        
        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
    /*
     * 根据商品id查询商品
     * */
    @RequestMapping(value="{itemId}",method=RequestMethod.GET)
    public ResponseEntity<Item> queryById(
            @PathVariable("itemId") Long itemId){
        try {
            Item item = this.itemService.queryById(itemId);
            if(null== item){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    /*
     * 根据商品 id删除商品
     * */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    public ResponseEntity<Void> deleteById(@RequestParam("ids") Long ids){
            try{
            Integer item = this.itemService.deleteById(ids);
            System.out.println("item:"+item);
            if(null == item){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
         // 成功 200
            return ResponseEntity.ok(null);
        }catch(Exception e){
            e.printStackTrace();
        }
     // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
