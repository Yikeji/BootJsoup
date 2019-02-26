package com.yldyyn.bootredis.controller;

import com.yldyyn.bootredis.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: yld
 * @Date: 2019-02-25 13:46
 * @Version 1.0
 */
@Controller
public class HelloController {
    @Resource
    private TestService testService;


    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @RequestMapping("getAllCourse")
    @ResponseBody
    public List<Map<String,Object>> GetAllCourse(){
        List<Map<String, Object>> allCourse = testService.getAllCourse();
        return allCourse;
    }
}
