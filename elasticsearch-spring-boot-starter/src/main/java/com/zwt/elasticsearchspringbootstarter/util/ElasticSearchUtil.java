package com.zwt.elasticsearchspringbootstarter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zwt.elasticsearchspringbootstarter.factory.ElasticSearchClientFactory;
import com.zwt.elasticsearchspringbootstarter.vo.DocumentVo;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zwt
 * @detail  ES工具类
 * @date 2018/8/30
 * @since 1.0
 */
public class ElasticSearchUtil {
    private final static Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);
    private static final SerializerFeature[] featuresWithNullValue={SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse,
            SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty};

    private ElasticSearchClientFactory elasticSearchClientFactory;

    public ElasticSearchUtil(ElasticSearchClientFactory elasticSearchClientFactory){
        this.elasticSearchClientFactory=elasticSearchClientFactory;
    }

    public Client getEsClient() {
        return elasticSearchClientFactory.getEsClient();
    }

    /**
     * 创建索引
     * @param index
     * @param type
     * @param id
     * @param isOnlyCreate
     * @param jsonString
     * @return
     */
    protected  boolean createDocument(String index,String type,String id,boolean isOnlyCreate,String jsonString){
        IndexResponse indexResponse;
        if(StringUtils.isBlank(id)){
            indexResponse=elasticSearchClientFactory.getEsClient().prepareIndex(index,type).setCreate(isOnlyCreate).setSource(jsonString, XContentType.JSON).get();
        }else{
            indexResponse=elasticSearchClientFactory.getEsClient().prepareIndex(index,type,id).setCreate(isOnlyCreate).setSource(jsonString,XContentType.JSON).get();
        }

        if(logger.isDebugEnabled()){
            String _index=indexResponse.getIndex();
            String _type=indexResponse.getType();
            String _id=indexResponse.getId();
            long _version = indexResponse.getVersion();
            boolean created = RestStatus.CREATED.equals(indexResponse.status());
            logger.debug(String.format("createDocument index:%s,type:%s,id:%s,version:%s,created:%s", _index, _type, _id, _version, created));
        }

        return RestStatus.CREATED.equals(indexResponse.status());
    }

    /**
     * 插入或更新文档
     * @param index
     * @param type
     * @param object
     * @return
     */
    public boolean insertOrUpdateDocument(String index, String type, Object object) {
        return this.insertOrUpdateDocument(index, type, null, object);
    }
    public boolean insertOrUpdateDocument(String index, String type, String id, Object object) {
        return this.createDocument(index, type, id, false, JSON.toJSONString(object, featuresWithNullValue));
    }
    public boolean insertOrUpdateDocument(String index, String type, String jsonString) {
        return this.insertOrUpdateDocument(index, type, null, jsonString);
    }
    public boolean insertOrUpdateDocument(String index, String type, String id, String jsonString) {
        return this.createDocument(index, type, id, false, jsonString);
    }

    /**
     * 新增文档
     * @param index
     * @param type
     * @param object
     * @return
     */
    public boolean insertDocument(String index, String type, Object object) {
        return this.insertDocument(index, type, null, object);
    }
    public boolean insertDocument(String index, String type, String id, Object object) {
        return this.createDocument(index, type, id, true, JSON.toJSONString(object, featuresWithNullValue));
    }
    public boolean insertDocument(String index, String type, String jsonString) {
        return this.insertDocument(index, type, null, jsonString);
    }
    public boolean insertDocument(String index, String type, String id, String jsonString) {
        return this.createDocument(index, type, id, true, jsonString);
    }


    /**
     * 批量创建文档
     * @param index
     * @param type
     * @param objectList
     */
    public void insertBatchObject(String index, String type, List<Object> objectList) {
        List<String> jsonStringList = new ArrayList<String>();
        for(Object object : objectList) {
            if (object != null) {
                jsonStringList.add(JSON.toJSONString(object, featuresWithNullValue));
            }
        }
        this.insertBatchDocument(index, type, jsonStringList);
    }

    /**
     * 批量创建文档，自动生成id
     * @param index
     * @param type
     * @param jsonStringList
     */
    public void insertBatchDocument(String index, String type, List<String> jsonStringList) {
        BulkRequestBuilder bulkRequestBuilder = elasticSearchClientFactory.getEsClient().prepareBulk();
        for(String json : jsonStringList) {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(index);
            indexRequest.type(type);
            indexRequest.source(json);
            bulkRequestBuilder.add(indexRequest);
        }
        BulkResponse bulkResponse = bulkRequestBuilder.get();
        if (bulkResponse.hasFailures()) {
            logger.warn(String.format("insertBatchDocument index:%s,type:%s,failureMessage:%s", index, type, bulkResponse.buildFailureMessage()));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("insertBatchDocument index:%s,type:%s,hasFailures:%s", index, type, bulkResponse.hasFailures()));
        }
    }

    /**
     * 批量创建文档，可以传入id
     * @param index
     * @param type
     * @param documentVoList
     */
    public void insertBatchDocumentVo(String index, String type, List<DocumentVo> documentVoList) {
        BulkRequestBuilder bulkRequestBuilder = elasticSearchClientFactory.getEsClient().prepareBulk();
        for(DocumentVo vo : documentVoList) {
            IndexRequest indexRequest = new IndexRequest();
            indexRequest.index(index);
            indexRequest.type(type);
            indexRequest.source(vo.getJson());
            if (StringUtils.isNotBlank(vo.getId())) {
                indexRequest.id(vo.getId());
            }
            bulkRequestBuilder.add(indexRequest);
        }
        BulkResponse bulkResponse = bulkRequestBuilder.get();
        if (bulkResponse.hasFailures()) {
            logger.warn(String.format("insertBatchDocument index:%s,type:%s,failureMessage:%s", index, type, bulkResponse.buildFailureMessage()));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("insertBatchDocument index:%s,type:%s,hasFailures:%s", index, type, bulkResponse.hasFailures()));
        }
    }

    /**
     * 删除文档
     * @param index
     * @param type
     * @param id
     */
    public void deleteDocument(String index, String type, String id) {
        DeleteResponse delResponse = elasticSearchClientFactory.getEsClient().prepareDelete(index, type, id).get();
        if (logger.isDebugEnabled()) {
            String _index = delResponse.getIndex();
            String _type = delResponse.getType();
            String _id = delResponse.getId();
            long _version = delResponse.getVersion();
            boolean found = RestStatus.FOUND.equals(delResponse.status());
            logger.debug(String.format("deleteIndex:index:%s,type:%s,id:%s,version:%s,found:%s", _index, _type, _id, _version, found));
        }
    }

    /**
     * 批量删除文档
     * @param index
     * @param type
     * @param idList
     */
    public void deleteBatchDocument(String index, String type, List<String> idList) {
        BulkRequestBuilder bulkRequestBuilder = elasticSearchClientFactory.getEsClient().prepareBulk();
        for(String id : idList) {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.index(index);
            deleteRequest.type(type);
            deleteRequest.id(id);
            bulkRequestBuilder.add(deleteRequest);
        }
        BulkResponse bulkResponse = bulkRequestBuilder.get();
        if (bulkResponse.hasFailures()) {
            logger.warn(String.format("deleteBatchDocument index:%s,type:%s,failureMessage:%s", index, type, bulkResponse.buildFailureMessage()));
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("deleteBatchDocument index:%s,type:%s,hasFailures:%s", index, type, bulkResponse.hasFailures()));
        }
    }

    /**
     * 检索文档
     * @param beanClass
     * @param index
     * @param type
     * @param queryBuilder
     * @param sortBuilderList
     * @param from
     * @param size
     * @param <T>
     * @return
     */
    public <T extends Object> List<T> search(Class<T> beanClass, String index, String type, QueryBuilder queryBuilder, List<SortBuilder> sortBuilderList, int from, int size) {
        List<T> retList = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder = elasticSearchClientFactory.getEsClient().prepareSearch(index).setQuery(queryBuilder);
        if (StringUtils.isNotBlank(type)) {
            searchRequestBuilder.setTypes(type);
        }
        if (sortBuilderList != null) {
            for(SortBuilder sortBuilder : sortBuilderList) {
                searchRequestBuilder.addSort(sortBuilder);
            }
        }
        searchRequestBuilder.setFrom(from);
        searchRequestBuilder.setSize(size);
        SearchResponse response = searchRequestBuilder.get();
        for(SearchHit searchHit : response.getHits().getHits()) {
            retList.add(convertJSONToObject(searchHit.getSourceAsString(), beanClass));
        }
        return retList;
    }

    public <T extends Object> List<T> search(Class<T> beanClass, String index, String type, String queryStr) {
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery(queryStr);
        return this.search(beanClass, index, type, queryBuilder, null);
    }
    public <T extends Object> List<T> search(Class<T> beanClass, String index, String type, QueryBuilder queryBuilder) {
        return this.search(beanClass, index, type, queryBuilder, null);
    }
    public <T extends Object> List<T> search(Class<T> beanClass, String index, String type, QueryBuilder queryBuilder, List<SortBuilder> sortBuilderList) {
        return this.search(beanClass, index, type, queryBuilder, sortBuilderList, 10);
    }
    public <T extends Object> List<T> search(Class<T> beanClass, String index, String type, QueryBuilder queryBuilder, List<SortBuilder> sortBuilderList, int size) {
        return this.search(beanClass, index, type, queryBuilder, sortBuilderList, 0, size);
    }

    public static <T> T convertJSONToObject(String data, Class<T> clzss) {
        try {
            T t = JSON.parseObject(data, clzss);
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 统计数量
     * @param index
     * @param type
     * @param queryBuilder
     * @return
     */
    public long count(String index, String type, QueryBuilder queryBuilder) {
        SearchRequestBuilder searchRequestBuilder = elasticSearchClientFactory.getEsClient().prepareSearch(index).setQuery(queryBuilder);
        if (StringUtils.isNotBlank(type)) {
            searchRequestBuilder.setTypes(type);
        }
        searchRequestBuilder.setFrom(0);
        searchRequestBuilder.setSize(0);
        SearchResponse response = searchRequestBuilder.get();
        return response.getHits().getTotalHits();
    }
}
