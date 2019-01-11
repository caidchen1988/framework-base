package com.zwt.frameworkdatasource;

import com.zwt.frameworkdatasource.service.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FrameworkDatasourceApplicationTests {

    @Autowired
    TestService testService;
    @Test
    public void contextLoads() {
        try{
            testService.doSomething1();
        }catch(Exception e){
            System.out.println(e);
        }
        try{
            testService.doSomething2();
        }catch(Exception e){
            System.out.println(e);
        }
    }
}

