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
package com.youkol.support.storage.oss.aliyun;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.StorageException;

/**
 * 阿里云对象存储
 * @see <a href="https://help.aliyun.com/document_detail/32008.html">阿里云对象存储开发文档</a>
 *
 * @author jackiea
 */
public class AliyunCloudStorageService extends AbstractStorageService<AliyunCloudStorageConfig> {

    private OSS ossClient;

    public AliyunCloudStorageService(AliyunCloudStorageConfig storageConfig) {
        super(storageConfig);
        this.init();
    }

    protected void init() {
        String endpoint = this.getStorageConfig().getEndpoint();
        String accessKeyId = this.getStorageConfig().getAccessKeyId();
        String accessKeySecret = this.getStorageConfig().getAccessKeySecret();

        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    protected void doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
            String bucketName = this.getStorageConfig().getBucketName();
            PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream);
            ossClient.putObject(request);
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    protected void doPutObject(String key, byte[] content) throws StorageException {
        putObject(key, new ByteArrayInputStream(content));
    }

    public OSS getOssClient() {
        return ossClient;
    }

}
