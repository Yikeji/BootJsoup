package com.yldyyn.bootredis.controller;

import com.yldyyn.bootredis.config.RedisConifg;
import com.yldyyn.bootredis.service.TestService;
import com.yldyyn.bootredis.utils.RedisUtils;
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
    @Resource
    RedisUtils redisUtils;

    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }

    @RequestMapping("getAllCourse")
    @ResponseBody
    public List<Map<String,Object>> GetAllCourse(){
        boolean set = redisUtils.set("name", "xxxx");
        if (set == true){
            System.out.println("缓存写入成功");
        }
        List<Map<String, Object>> allCourse = testService.getAllCourse();
        String key = "name";
        System.out.println("缓存中获取到的值："+redisUtils.get(key));
        return allCourse;

    }
}
