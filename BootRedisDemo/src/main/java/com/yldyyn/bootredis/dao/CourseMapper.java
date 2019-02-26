package com.yldyyn.bootredis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author: yld
 * @Date: 2019-02-25 14:16
 * @Version 1.0
 */
@Mapper
public interface CourseMapper {

    @Select("SELECT * FROM course")
    public List<Map<String,Object>> getAllCourse();

    @Select("SELECT * FROM course WHERE CID = #{CID}")
    public List<Map<String,Object>> findAllByCId(String CID);
}
