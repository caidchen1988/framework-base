package com.zwt.elasticsearchspringbootstarter.factory;

import com.zwt.elasticsearchspringbootstarter.exception.ElasticSearchException;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author zwt
 * @detail ESClient工厂类
 * @date 2018/8/30
 * @since 1.0
 */
public class ElasticSearchClientFactory {
    private final static Logger logger = LoggerFactory.getLogger(ElasticSearchClientFactory.class);

    private volatile TransportClient esClient;
    //ES配置
    private ElasticSearchConfiguration esConfig;

    //校验多个ES地址的正则
    private Pattern addRessPattern = Pattern.compile("^.+[:]\\d{1,5}\\s*(;.+[:]\\d{1,5}\\s*)*[;]?\\s*$");

    public ElasticSearchClientFactory(final ElasticSearchConfiguration elasticSearchConfiguration){
        this.esConfig=elasticSearchConfiguration;
    }

    /**
     * 获取一个单例的ESClient
     * @return
     */
    public Client getEsClient(){
        if(esClient==null){
            synchronized (ElasticSearchClientFactory.class){
                if(esClient==null){
                    logger.info("ElasticSearchClientFactory init start...");
                    try{
                        logger.info("ESConfig is:{}",esConfig.toString());
                        //多个ES地址解析
                        List<HostAndPort> hostAndPortList = this.parseHostAndPortList(esConfig.getAddress());
                        TransportAddress [] transportAddress=new TransportAddress[hostAndPortList.size()];
                        for (int i = 0; i < hostAndPortList.size(); i++) {
                            transportAddress[i] = new TransportAddress(InetAddress.getByName(hostAndPortList.get(i).getIp()), hostAndPortList.get(i).getPort());
                        }
                        //节点名
                        String nodeName=esConfig.getNodeName()+ UUID.randomUUID();
                        String clusterName=esConfig.getClusterName();
                        Settings.Builder settingsBuilder = Settings.builder();
                        settingsBuilder.put("node.name", nodeName);
                        if(StringUtils.isNotBlank(clusterName)){
                            settingsBuilder.put("cluster.name", clusterName);
                        }
                        settingsBuilder.put("client.transport.sniff", true);
                        Settings settings = settingsBuilder.build();
                        TransportClient client = new PreBuiltTransportClient(settings);
                        //创建ESClient
                        esClient = client.addTransportAddresses(transportAddress);
                        logger.info("EalsticSearchClientFactory init is finished");
                    }catch(Exception e){
                        logger.error("EalsticSearchClientFactory create failed",e);
                        throw new ElasticSearchException("EalsticSearchClientFactory create faile",e);
                    }
                }
            }
        }
        return esClient;
    }

    /**
     * 解析每个ES的端口和地址
     * @param addressContent
     * @return
     * @throws Exception
     */
    private List<HostAndPort> parseHostAndPortList(String addressContent) throws  Exception{
        try {
            if (StringUtils.isBlank(addressContent)) {
                throw new IllegalArgumentException("address 连接地址配置不能为空");
            }
            boolean result = addRessPattern.matcher(addressContent).matches();
            if (!result) {
                throw new IllegalArgumentException("address 连接地址配置不合法");
            }
            List<HostAndPort> hostAndPortList = new ArrayList<HostAndPort>();
            String[] addressArrays = addressContent.split(";");
            for (String address : addressArrays) {
                String[] ipAndPort = address.trim().split(":");
                String ip = ipAndPort[0].trim();
                String port = ipAndPort[1].trim();
                HostAndPort hostAndPort = new HostAndPort(ip, Integer.parseInt(port));
                hostAndPortList.add(hostAndPort);
            }
            return hostAndPortList;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Exception("解析address配置失败", ex);
        }
    }

}
