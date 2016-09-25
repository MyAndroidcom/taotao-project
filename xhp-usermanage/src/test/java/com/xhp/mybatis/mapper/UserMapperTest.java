package com.xhp.mybatis.mapper;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.abel533.entity.Example;
import com.xhp.mybatis.pojo.User;

public class UserMapperTest {

    private UserMapper userMapper;
    
    @Before
    public void setUp() throws Exception {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext*.xml");
        this.userMapper = ac.getBean(UserMapper.class);
    }

    @Test
    public void testSelectOne() {
        User record = new User();
        record.setId(1L);
        User user = this.userMapper.selectOne(record);
        System.out.println(user);
    }

    @Test
    public void testSelect() {
            
    }

    @Test
    public void testSelectCount() {
        fail("Not yet implemented");
    }

    @Test
    public void testSelectByPrimaryKey() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsert() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsertSelective() {
        fail("Not yet implemented");
    }

    @Test
    public void testDelete() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteByPrimaryKey() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateByPrimaryKey() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        fail("Not yet implemented");
    }

    @Test
    public void testSelectCountByExample() {
        fail("Not yet implemented");
    }

    @Test
    public void testDeleteByExample() {
        fail("Not yet implemented");
    }

    @Test
    public void testSelectByExample() {
        Example example = new Example(User.class);
        List<Object> values = new ArrayList<Object>();
        values.add(1L);
//        values.add(2L);
//        values.add(3L);
        example.createCriteria().andEqualTo("id", values);
        List<User> list = this.userMapper.selectByExample(example);
        for (User user : list) {
            System.out.println(user);
        }
    }

    @Test
    public void testUpdateByExampleSelective() {
        fail("Not yet implemented");
    }

    @Test
    public void testUpdateByExample() {
        fail("Not yet implemented");
    }

}
