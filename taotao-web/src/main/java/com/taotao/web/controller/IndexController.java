package com.taotao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.web.service.IndexService;

@Controller
public class IndexController {

    @Autowired
    private IndexService IndexService;

    @RequestMapping(value="index",method=RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("index");
        
        //大广告位置
        String indexAd1 = this.IndexService.queryIndexAD1();
        mv.addObject("indexAD1",indexAd1);
        
        //淘淘快报显示
        mv.addObject("indexAD2", this.IndexService.queryIndexAD2());;
        return mv;
    }
}
