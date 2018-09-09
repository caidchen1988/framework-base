package com.zwt.framework.es.factory;

/**
 * @author zwt
 * @detail ES配置Bean
 * @date 2018/8/30
 * @since 1.0
 */
public class ElasticSearchConfiguration {
    /**
     * 本地属性文件路径.
     */
    private String localPropertiesPath;

    /**
     * ip 及端口地址 ，分号分隔地址 示例：127.0.0.1:9300;127.0.0.1:9300
     */
    private String address;
    /**
     * es的集群名称
     */
    private String clusterName;

    /**
     * es的节点名字
     */
    private String nodeName;

    public String getLocalPropertiesPath() {
        return localPropertiesPath;
    }

    public void setLocalPropertiesPath(String localPropertiesPath) {
        this.localPropertiesPath = localPropertiesPath;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("ElasticSearchConfiguration [localPropertiesPath=");
        builder.append(localPropertiesPath);
        builder.append(", address=");
        builder.append(address);
        builder.append(", nodeName=");
        builder.append(nodeName);
        builder.append("]");
        return builder.toString();
    }
}
