package com.zwt.ossutils.upload;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;

import java.io.File;

/**
 * @author zwt
 * @detail 微软Azure文件上传
 * @date 2019/4/30
 * @since 1.0
 */
public class AzureUploadUtil extends UploadAbstractUtil{
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
     * Azure Client
     */
    private CloudBlobClient blobClient;

    /**
     * 构造器
     * @param basedir
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
    }

    /**
     * 上传文件到Azure
     * @param
     * @return
     */
    @Override
    public String upload(File tempFile,String realName) {
        try {
            if(blobClient == null){
                CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
                blobClient = storageAccount.createCloudBlobClient();
            }
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
        } catch (StorageException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
