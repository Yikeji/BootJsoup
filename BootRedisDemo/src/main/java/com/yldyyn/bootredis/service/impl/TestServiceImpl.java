package com.yldyyn.bootredis.service.impl;

import com.yldyyn.bootredis.dao.CourseMapper;
import com.yldyyn.bootredis.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: yld
 * @Date: 2019-02-25 14:12
 * @Version 1.0
 */
@Service
public class TestServiceImpl implements TestService {
    @Resource
    private CourseMapper courseMapper;
    @Override
    public List<Map<String,Object>> getAllCourse(){
        List<Map<String, Object>> allCourse = courseMapper.getAllCourse();
        return allCourse;
    }


}
