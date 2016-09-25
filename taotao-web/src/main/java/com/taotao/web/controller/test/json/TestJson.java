package com.taotao.web.controller.test.json;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class TestJson {
    @RequestMapping("test.html")
    @ResponseBody
    public String testJson(String callback) {
        // test("aa")
        return callback + "(" + "'aa'" + ")";
    }
}
