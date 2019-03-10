package com.zwt.configcenterserver.controller;

import com.zwt.configcenterserver.util.ConfigUtil;
import com.zwt.configcenterserver.vo.DataVo;
import com.zwt.configcenterserver.vo.KeyListVo;
import com.zwt.configcenterserver.vo.ResultVo;
import com.zwt.framework.zk.client.impl.CuratorZKClient;
import com.zwt.framework.zk.config.ZKConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zwt
 * @detail
 * @date 2018/12/25
 * @since 1.0
 */
@Controller
@RequestMapping("/main")
public class MainController {
    @RequestMapping("/getKeyList")
    @ResponseBody
    public KeyListVo getKeyList(HttpServletRequest request, HttpServletResponse response){
        KeyListVo keyListVo = new KeyListVo();
        String connectString=request.getParameter("connectString");
        String nameSpace=request.getParameter("nameSpace");
        String key=request.getParameter("key");
        ZKConfig zkConfig = new ZKConfig();
        zkConfig.setConnectString(connectString);
        zkConfig.setNameSpace(nameSpace);
        CuratorZKClient curatorZKClient = new CuratorZKClient(zkConfig);
        curatorZKClient.start();
        if(StringUtils.isBlank(key)){
            keyListVo.setData(curatorZKClient.getNodes(ConfigUtil.getConfigCenterPath()));
        }else{
            keyListVo.setData(curatorZKClient.getNodes(ConfigUtil.joinPath(ConfigUtil.getConfigCenterPath(),key)));
        }
        keyListVo.setCode("1");
        keyListVo.setMessage("success");
        return keyListVo;
    }

    @RequestMapping("/showZKData")
    @ResponseBody
    public ResultVo showZKData(HttpServletRequest request){
        String connectString=request.getParameter("connectString");
        String nameSpace=request.getParameter("nameSpace");
        String key=request.getParameter("key");
        ZKConfig zkConfig = new ZKConfig();
        zkConfig.setConnectString(connectString);
        zkConfig.setNameSpace(nameSpace);
        CuratorZKClient curatorZKClient = new CuratorZKClient(zkConfig);
        curatorZKClient.start();
        String value = curatorZKClient.getStringData(ConfigUtil.joinPath(ConfigUtil.getConfigCenterPath(),key));

        DataVo data =new DataVo();
        data.setValue(value);
        ResultVo resultVo = new ResultVo();
        resultVo.setCode("1");
        resultVo.setMessage("success");
        resultVo.setData(data);
        return resultVo;
    }
}
