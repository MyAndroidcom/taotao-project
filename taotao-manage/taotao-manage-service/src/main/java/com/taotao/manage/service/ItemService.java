package com.taotao.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService2<Item> {
    @Autowired
    private ItemDescService itemDescService;
    
    @Autowired
    RabbitTemplate rabbitTemplate;
    
    @Autowired
    private ItemParamService itemParamService;
    
    @Autowired
    private ItemParamItemService itemParamItemService;
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private ApiService apiService;
    
    @Autowired
    private ItemMapper itemMapper;
    
    @Value("${TAOTAO_WEB_URL}")
    private String TAOTAO_WEB_URL;
    
    //查询商品的service层
    public EasyUIResult queryPageList(Integer page, Integer rows) {
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");
        //设置分页参数
        PageHelper.startPage(page,rows);
        
        List<Item> list = this.itemMapper.selectByExample(example);
        PageInfo<Item> pageInfo =  new PageInfo<>(list);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
    
    //编辑商品的service层
    public void editItem(Item item, String desc,String itemParams) {
        // 强制设置不能修改的字段为空
        item.setStatus(null);
        item.setCreated(null);
        super.updateSelective(item);

        // 修改商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.updateSelective(itemDesc);
        
        //修改商品规格参数数据
        this.itemParamItemService.updateItemParamItem(item.getId(),itemParams);
        
        try{
            //通知其他系统该商品已经更新
            String url = TAOTAO_WEB_URL + "/item/cache"+item.getId() + ".html";
//            System.out.println(url+"=============================================");
            
            this.apiService.doPost(url, null);
                   
        }catch(Exception e){
            e.printStackTrace();
        }
        // 发送消息
        sendMsg(item.getId(), "update");
    }

    public void saveItem(Item item, String desc, String itemParams) {
            //设置初始数据
        item.setStatus(1);
        item.setId(null);
        super.save(item);
        
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        //保存
        this.itemDescService.save(itemDesc);
        
        //保存规格数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        //修改商品的规格参数数据
        this.itemParamItemService.save(itemParamItem);
       

        // 发送消息
        sendMsg(item.getId(), "insert");
    }
    
    private void sendMsg(Long itemId,String type){
        try {
        //调用rabbitMQ发送消息,发送商品的id,操作类型
        Map<String,Object> msg = new HashMap<String,Object>();
        msg.put("itemId", itemId);
        msg.put("type", "update");
        msg.put("date", System.currentTimeMillis());
        this.rabbitTemplate.convertAndSend("item." + type,MAPPER.writeValueAsString(msg));
    }  catch (Exception e) {
        e.printStackTrace();
    }
    }
}
