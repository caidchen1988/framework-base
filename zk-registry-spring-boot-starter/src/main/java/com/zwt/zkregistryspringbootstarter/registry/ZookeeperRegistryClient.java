package com.zwt.zkregistryspringbootstarter.registry;

import com.zwt.framework.zk.client.impl.CuratorZKClient;
import com.zwt.framework.zk.config.ZKConfig;
import com.zwt.zkregistryspringbootstarter.exception.ZookeeperRegistryException;
import com.zwt.zkregistryspringbootstarter.util.ZKRegistryUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * @author zwt
 * @detail 服务注册
 * @date 2018/12/27
 * @since 1.0
 */
public class ZookeeperRegistryClient {

    private CuratorZKClient client;

    public ZookeeperRegistryClient(ZKConfig zkConfig){
        client = new CuratorZKClient(zkConfig);
        client.start();
    }

    public CuratorZKClient getCuratorZKClient(){
        return client;
    }

    /**
     * 注册服务
     * @param serviceName
     * @param serviceAddress
     */
    public void register(String serviceName, String serviceAddress){
        if(StringUtils.isBlank(serviceName)||StringUtils.isBlank(serviceAddress)){
            throw new ZookeeperRegistryException("参数错误");
        }
        //检查注册中心节点是否存在，不存在就创建一个
        String registryPath = ZKRegistryUtil.getZKRegistryPath();
        if(!client.checkNodeExist(registryPath)){
            client.createNode(registryPath);
        }
        //检查服务节点，如果不存在，会创建
        String servicePath = ZKRegistryUtil.joinPath(registryPath,serviceName);
        if(!client.checkNodeExist(servicePath)){
            client.createNode(servicePath);
        }
        //创建地址节点并赋值
        String addressPath = ZKRegistryUtil.joinPath(servicePath,ZKRegistryUtil.ADDRESS_PATH);
        client.createSequenceEphemeralNode(addressPath,serviceAddress.getBytes());
    }

    /**
     * 获取一个服务地址，没有serviceName请传方法名
     * @param serviceName
     * @return
     */
    public String getServiceAddress(String serviceName){
        if(StringUtils.isBlank(serviceName)){
            throw new ZookeeperRegistryException("参数错误！");
        }
        String tempServiceName = serviceName.toUpperCase() + ZKRegistryUtil.HTTP_SERVICE_SUFFIX;
        String servicePath = ZKRegistryUtil.joinPath(ZKRegistryUtil.getZKRegistryPath(),tempServiceName);
        List<String> list = client.getNodes(servicePath);
        if(CollectionUtils.isEmpty(list)){
            throw new ZookeeperRegistryException("服务不存在！");
        }
        Random random = new Random();
        String addressPath = ZKRegistryUtil.joinPath(servicePath,list.get(random.nextInt(list.size())));
        return client.getStringData(addressPath);
    }
}
