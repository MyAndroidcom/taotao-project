package com.taotao.manage.controller;

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
import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamItemService;
import com.taotao.manage.service.ItemParamService;

@Controller
@RequestMapping("item/param")
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;

    
    @Autowired
    private ItemParamItemService itemParamItemService;
    /*
     * 根据类目id查找规格参数模板
     */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.GET)
    public ResponseEntity<ItemParam> queryByItemId(
            @PathVariable("itemCatId") Long itemCatId){
        try{
            ItemParam record = new ItemParam();
            record.setItemCatId(itemCatId);
            ItemParam itemParam = this.itemParamService.queryOne(record);
            if(null == itemParam){
                //404 not_found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            //200 该类目已经添加
            return ResponseEntity.ok(itemParam);
        }catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    /*
     * 
     * 新增规格模板参数
     * */
    @RequestMapping(value="{itemCatId}",method=RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(
            @PathVariable("itemCatId") Long itemCatId,@RequestParam("paramData") String paramData){
        try{
            ItemParam itemParam = new ItemParam();
            itemParam.setItemCatId(itemCatId);
            itemParam.setParamData(paramData);
            this.itemParamService.save(itemParam);
            //201   新增商品规格成功!
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch(Exception e){
            e.printStackTrace();
        }
        //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /*
     * 查询规格参数列表
     * */
    @RequestMapping(value="list",method=RequestMethod.GET)
    @ResponseBody
    public EasyUIResult queryItemList(
            @RequestParam(value="page",defaultValue="1") Integer page,
            @RequestParam(value="rows",defaultValue="30") Integer rows){
        return this.itemParamItemService.queryPageList(page, rows);
      /*  try{
            EasyUIResult easyui =  this.itemService.queryPageList(page, rows);
            return ResponseEntity.ok(easyui);
        }catch(Exception e){
            e.printStackTrace();
        }
        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);*/
    }
    
}
