package com.taotao.manage.service;

import java.util.Date;
import java.util.List;

import com.github.abel533.mapper.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.pojo.BasePojo;

public abstract class BaseService <T extends BasePojo>{
    
    public abstract Mapper<T> getMapper();

    /*
     * 根据id查询数据
     * */
    public T queryById(Long id){
        return this.getMapper().selectByPrimaryKey(id);
    }
    /*
     * 查询所有数据
     * 
     */
    public List<T> queryAll(){
        return this.getMapper().select(null);
    }
    /*
     * 根据条件查询一条数据
     * */
    public T queryOne(T record){
        return this.getMapper().selectOne(record);
    }
    /*
     * 根据条件查询列表
     * */
    public List<T> queryListByWhere(T record){
        return this.getMapper().select(record);
    }
    /*
     * 分页查询
     * */
    public PageInfo<T> queryPageListByWhere(Integer page,Integer rows,T record){
        //设置分页查询条件
        PageHelper.startPage(page,rows);
        List<T> list = this.queryListByWhere(record);
        return new PageInfo<T>(list);
    }
    /*
     * 新增数据,返回成功添加的条数
     * */
    public Integer save(T record){
        record.setCreated(new Date());
        record.setUpdated(record.getCreated());
        return this.getMapper().insert(record);
    }
    
    
}
