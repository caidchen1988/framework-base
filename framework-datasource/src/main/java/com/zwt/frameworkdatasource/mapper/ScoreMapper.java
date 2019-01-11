package com.zwt.frameworkdatasource.mapper;

import com.zwt.frameworkdatasource.model.ScoreCriteria;
import com.zwt.frameworkdatasource.model.ScoreModel;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
* ScoreMapper
* 
* @version v1.0
* @copy pet
* @date 2019-01-04 18:00:01
*/
public interface ScoreMapper {
    /**
     * 根据条件查询记录总数
     */
    int countByExample(ScoreCriteria scoreCriteria);

    /**
     * 根据条件删除记录
     */
    int deleteByExample(ScoreCriteria scoreCriteria);

    /**
     * 根据主键删除记录
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 保存记录,不管记录里面的属性是否为空
     */
    int insert(ScoreModel scoreModel);

    /**
     * 保存属性不为空的记录
     */
    int insertSelective(ScoreModel scoreModel);

    /**
     * 根据条件查询记录集
     */
    List<ScoreModel> selectByExample(ScoreCriteria scoreCriteria);

    /**
     * 根据主键查询记录
     */
    ScoreModel selectByPrimaryKey(Integer id);

    /**
     * 根据条件更新属性不为空的记录
     */
    int updateByExampleSelective(@Param("record") ScoreModel scoreModel, @Param("example") ScoreCriteria scoreCriteria);

    /**
     * 根据条件更新记录
     */
    int updateByExample(@Param("record") ScoreModel scoreModel, @Param("example") ScoreCriteria scoreCriteria);

    /**
     * 根据主键更新属性不为空的记录
     */
    int updateByPrimaryKeySelective(ScoreModel scoreModel);

    /**
     * 根据主键更新记录
     */
    int updateByPrimaryKey(ScoreModel scoreModel);
}