package com.yld.jsoup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: yld
 * @Date: 2019-03-08 09:51
 * @Version 1.0
 */
@Controller
public class PageController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
