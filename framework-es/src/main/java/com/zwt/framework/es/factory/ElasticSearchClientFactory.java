package com.zwt.framework.es.factory;

import com.zwt.framework.es.exception.ElasticSearchException;
import com.zwt.framework.utils.util.PropertiesUtils;
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
import java.util.Properties;
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
    private ElasticSearchConfiguration esConfig;

    private Pattern addRessPattern = Pattern.compile("^.+[:]\\d{1,5}\\s*(;.+[:]\\d{1,5}\\s*)*[;]?\\s*$");

    public ElasticSearchClientFactory(final ElasticSearchConfiguration elasticSearchConfiguration){
        this.esConfig=elasticSearchConfiguration;
    }

    public Client getEsClient(){
        if(esClient==null){
            synchronized (ElasticSearchClientFactory.class){
                if(esClient==null){
                    logger.info("ElasticSearchClientFactory init start...");
                    try{
                        if(StringUtils.isNotBlank(esConfig.getLocalPropertiesPath())){
                            fillData();
                        }
                        logger.info("ESConfig is:{}",esConfig.toString());
                        List<HostAndPort> hostAndPortList = this.parseHostAndPortList(esConfig.getAddress());
                        TransportAddress [] transportAddress=new TransportAddress[hostAndPortList.size()];
                        for (int i = 0; i < hostAndPortList.size(); i++) {
                            transportAddress[i] = new TransportAddress(InetAddress.getByName(hostAndPortList.get(i).getIp()), hostAndPortList.get(i).getPort());
                        }
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
                        esClient = client.addTransportAddresses(transportAddress);
                        logger.info("EalsticSearchClientFactory init is finished");
                    }catch(Exception e){
                        throw new ElasticSearchException(e);
                    }
                }
            }
        }
        return esClient;
    }

    private void fillData() throws ElasticSearchException {

        Properties localProperties = PropertiesUtils.loadLocalProperties(esConfig.getLocalPropertiesPath());

        String address = localProperties.getProperty("address", "");
        if (StringUtils.isBlank(address)) {
            throw new ElasticSearchException("error:elasticSearch config address is blank!");
        }

        String nodeName=localProperties.getProperty("nodeName","");
        String clusterName=localProperties.getProperty("clusterName","");

        esConfig.setAddress(address);
        esConfig.setNodeName(nodeName);
        esConfig.setClusterName(clusterName);
    }

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
