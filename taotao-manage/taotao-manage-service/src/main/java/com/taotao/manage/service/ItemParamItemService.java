package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.mapper.ItemParamItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemParamItem;


@Service
public class ItemParamItemService extends BaseService2<ItemParamItem>{

    @Autowired
    private ItemParamItemMapper itemParamItemMapper;
    
    public void updateItemParamItem(Long itemId, String itemParams) {
        //更新对象
        ItemParamItem record = new ItemParamItem();
        record.setParamData(itemParams);
        record.setUpdated(new Date());
        
        //更新条件
        Example example = new Example(ItemParamItem.class);
        example.createCriteria().andEqualTo("itemId", itemId);
        this.itemParamItemMapper.updateByExampleSelective(record, example);
    }

    public EasyUIResult queryPageList(Integer page, Integer rows) {
        Example example = new Example(ItemParamItem.class);
        example.setOrderByClause("updated DESC");
        //设置分页参数
        PageHelper.startPage(page,rows);
        
        List<ItemParamItem> list = this.itemParamItemMapper.selectByExample(example);
        PageInfo<ItemParamItem> pageInfo =  new PageInfo<>(list);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
    
}
