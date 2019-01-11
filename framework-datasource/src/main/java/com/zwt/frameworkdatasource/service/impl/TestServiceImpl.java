package com.zwt.frameworkdatasource.service.impl;

import com.zwt.frameworkdatasource.mapper.ScoreMapper;
import com.zwt.frameworkdatasource.model.ScoreModel;
import com.zwt.frameworkdatasource.multipleDataSource.DatabaseType;
import com.zwt.frameworkdatasource.multipleDataSource.TargetDataSource;
import com.zwt.frameworkdatasource.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zwt
 * @detail
 * @date 2019/1/4
 * @since 1.0
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private ScoreMapper scoreMapper;
    boolean flag = false;
    @Override
    @TargetDataSource(database = DatabaseType.MASTER)
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void doSomething1() {
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setId(1234);
        scoreMapper.insert(scoreModel);
        if(!flag){
            throw new RuntimeException("出现异常");
        }
    }
    @Override
    @TargetDataSource(database = DatabaseType.SLAVE)
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public void doSomething2() {
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setId(12345);
        scoreMapper.insert(scoreModel);
        if(flag){
            throw new RuntimeException("出现异常");
        }
    }
}
