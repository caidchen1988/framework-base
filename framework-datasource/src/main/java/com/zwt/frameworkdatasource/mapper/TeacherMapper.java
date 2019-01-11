package com.zwt.frameworkdatasource.mapper;

import com.zwt.frameworkdatasource.model.TeacherCriteria;
import com.zwt.frameworkdatasource.model.TeacherModel;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
* TeacherMapper
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public interface TeacherMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByExample(TeacherCriteria teacherCriteria);

    /**
     * 根据条件删除记录
     */
    int deleteByExample(TeacherCriteria teacherCriteria);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(TeacherModel teacherModel);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(TeacherModel teacherModel);

    /**
     * 根据条件查询记录集
     */
    List<TeacherModel> selectByExample(TeacherCriteria teacherCriteria);

    /**
     * 根据主键查询记录
     */
    TeacherModel selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") TeacherModel teacherModel, @Param("example") TeacherCriteria teacherCriteria);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") TeacherModel teacherModel, @Param("example") TeacherCriteria teacherCriteria);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(TeacherModel teacherModel);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(TeacherModel teacherModel);
}