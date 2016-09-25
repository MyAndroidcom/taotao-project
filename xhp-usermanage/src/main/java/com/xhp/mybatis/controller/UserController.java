package com.xhp.mybatis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xhp.mybatis.bean.EasyUIResult;
import com.xhp.mybatis.cervice.UserService;

@Controller
@RequestMapping(value="/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @RequestMapping(value="/list",method=RequestMethod.GET)
    @ResponseBody
    public EasyUIResult queryList(
            @RequestParam(value="page",defaultValue="1") Integer pages,
            @RequestParam(value="rows",defaultValue="5")Integer rows){
        return this.userService.queryUserList(pages, rows);
    }
}
