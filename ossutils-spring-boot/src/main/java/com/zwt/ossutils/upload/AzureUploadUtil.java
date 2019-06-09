package com.zwt.ossutils.upload;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.UUID;

/**
 * @author zwt
 * @detail 微软Azure文件上传
 * @date 2019/4/30
 * @since 1.0
 */
public class AzureUploadUtil extends AbstractUploadUtil {
    /**
     * 用户名
     */
    private  String accountName;
    /**
     * 密码
     */
    private  String accountKey;
    /**
     * endPoint
     */
    private  String endPoint;
    /**
     * containerName
     */
    private  String containerName;
    /**
     * 连接串
     */
    private String storageConnectionString;

    /**
     * Azure client
     */
    private CloudBlobClient blobClient;

    /**
     * 构造器
     * @param accountName
     * @param accountKey
     * @param endPoint
     * @param containerName
     */
    public AzureUploadUtil(String basedir,String accountName, String accountKey, String endPoint, String containerName) {
        super(basedir);
        this.accountName = accountName;
        this.accountKey = accountKey;
        this.endPoint = endPoint;
        this.containerName = containerName;
        this.storageConnectionString = "DefaultEndpointsProtocol=https;AccountName="+ accountName +";AccountKey="+ accountKey +";EndpointSuffix=" + endPoint;
        initClient();
    }

    /**
     * 上传文件到Azure
     * @param
     * @return
     */
    @Override
    protected String upload(File tempFile,String realName) {
        try {
            initClient();
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            // Create the container if it does not exist with public access.
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
            //Getting a blob reference
            CloudBlockBlob blob = container.getBlockBlobReference(tempFile.getName());
            //Creating blob and uploading file to it
            blob.uploadFromFile(tempFile.getAbsolutePath());
            String imageUrl = "https://"+ accountName +".blob."+ endPoint +"/"+ containerName +"/" + tempFile.getName();
            logger.info("微软Azure上传服务------图片url:[{}]", imageUrl);
            return imageUrl;
        } catch (IOException| StorageException | URISyntaxException e) {
            logger.error("使用微软Azure上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传文件到Azure
     * @param file
     * @return
     */
    @Override
    String upload(MultipartFile file) {
        initClient();
        String key = generateUploadFileName(file);
        try(InputStream is = new ByteArrayInputStream(file.getBytes())){
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            // Create the container if it does not exist with public access.
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
            CloudBlockBlob blob = container.getBlockBlobReference(key);
            //Creating blob and uploading file to it
            blob.upload(is, is.available());
            return "https://"+ accountName +".blob."+ endPoint +"/"+ containerName +"/" + key;
        }catch (IOException| StorageException | URISyntaxException e){
            logger.error("使用微软Azure上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 尝试初始化client
     */
    @Override
    protected void initClient(){
        if(blobClient == null){
            try{
                CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
                blobClient = storageAccount.createCloudBlobClient();
                BlobRequestOptions blobRequestOptions = new BlobRequestOptions();
                blobRequestOptions.setTimeoutIntervalInMs(UPLOAD_TIMEOUT);
                blobClient.setDefaultRequestOptions(blobRequestOptions);
            }catch (URISyntaxException|InvalidKeyException e){
                logger.error("使用微软Azure初始化client失败！",e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected String upload(byte[] bytes, String contentType) {
        initClient();
        String realName = UUID.randomUUID().toString() + IMAGE_JPG;
        try(InputStream is = new ByteArrayInputStream(bytes)){
            CloudBlobContainer container = blobClient.getContainerReference(containerName);
            // Create the container if it does not exist with public access.
            container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());
            CloudBlockBlob blob = container.getBlockBlobReference(realName);
            //Creating blob and uploading file to it
            blob.upload(is, is.available());
            return "https://"+ accountName +".blob."+ endPoint +"/"+ containerName +"/" + realName;
        }catch (IOException| StorageException | URISyntaxException e){
            logger.error("使用微软Azure上传文件出现异常",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void shutdown() {
        blobClient = null;
    }
}
