package com.yldyyn.bootredis.controller;

import com.yldyyn.bootredis.config.RedisConifg;
import com.yldyyn.bootredis.service.TestService;
import com.yldyyn.bootredis.utils.RedisUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/returnModel")
    public ModelAndView returnModel(){
        ModelAndView modelAndView = new ModelAndView("/index");
        String testname = "三";
        String testage = "18";
        modelAndView.addObject("testname",testname);
        modelAndView.addObject("testage",testage);
        return modelAndView;
    }

    @GetMapping("templates")
    public String testmodel(HttpServletRequest request,Model model){
        request.setAttribute("key","测试数据");
        return "index";
    }
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        String testname = "三";
        model.addAttribute("name", testname);
        return "index";
    }

}
