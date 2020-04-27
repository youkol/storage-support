/**
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.youkol.support.storage.oss.tencent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.StorageException;

/**
 * 腾讯云对象存储
 * @see <a href="https://cloud.tencent.com/document/product/436/10199">腾讯云对象存储开发文档</a>
 *
 * @author jackiea
 */
public class TencentCloudStorageService extends AbstractStorageService<TencentCloudStorageConfig> {

    private COSClient cosClient;

    public TencentCloudStorageService(TencentCloudStorageConfig storageConfig) {
        super(storageConfig);
        this.init();
    }

    protected void init() {
        String regionName = this.getStorageConfig().getRegionName();
        String secretId = this.getStorageConfig().getSecretId();
        String secretKey = this.getStorageConfig().getSecretKey();

        Region region = new Region(regionName);
        ClientConfig clientConfig = new ClientConfig(region);
        COSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);
        cosClient = new COSClient(credentials, clientConfig);
    }

    @Override
    protected void doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
            String bucketName = this.getStorageConfig().getBucketName();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, new ObjectMetadata());
            cosClient.putObject(putObjectRequest);
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    protected void doPutObject(String key, byte[] content) throws StorageException {
        putObject(key, new ByteArrayInputStream(content));
    }

    public COSClient getCosClient() {
        return cosClient;
    }

}
