package com.zwt.frameworkdatasource.mapper;

import com.zwt.frameworkdatasource.model.CourseCriteria;
import com.zwt.frameworkdatasource.model.CourseModel;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
* CourseMapper
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public interface CourseMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByExample(CourseCriteria courseCriteria);

    /**
     * 根据条件删除记录
     */
    int deleteByExample(CourseCriteria courseCriteria);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(CourseModel courseModel);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(CourseModel courseModel);

    /**
     * 根据条件查询记录集
     */
    List<CourseModel> selectByExample(CourseCriteria courseCriteria);

    /**
     * 根据主键查询记录
     */
    CourseModel selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") CourseModel courseModel, @Param("example") CourseCriteria courseCriteria);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") CourseModel courseModel, @Param("example") CourseCriteria courseCriteria);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(CourseModel courseModel);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(CourseModel courseModel);
}