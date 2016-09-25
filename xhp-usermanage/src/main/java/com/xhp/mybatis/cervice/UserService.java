package com.xhp.mybatis.cervice;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xhp.mybatis.bean.EasyUIResult;
import com.xhp.mybatis.mapper.UserMapper;
import com.xhp.mybatis.pojo.User;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public EasyUIResult queryUserList(Integer page,Integer rows){
        //设置分页参数
        PageHelper.startPage(page,rows);
        //设置查询参数
        Example example = new Example(User.class);
        example.setOrderByClause("created DESC");
        List<User> list = this.userMapper.selectByExample(example);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        return new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
    }
    public User queryUserById(Long id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    public void saveUser(User user) {
        user.setCreated(new Date());
        user.setUpdated(new Date());
        this.userMapper.insertSelective(user);
    }

    public void updateUser(User user) {
        this.userMapper.updateByPrimaryKeySelective(user);
    }

    public void deleteUserById(Long id) {
        this.userMapper.deleteByPrimaryKey(id);
    }
}
