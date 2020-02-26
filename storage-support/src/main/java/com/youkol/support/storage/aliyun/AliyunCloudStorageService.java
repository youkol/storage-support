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
package com.youkol.support.storage.aliyun;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.exception.StorageException;

/**
 * 阿里云存储
 *
 * @see https://help.aliyun.com/document_detail/32008.html
 * @author jackiea
 */
public class AliyunCloudStorageService extends AbstractStorageService {

    private String accessKeyId;

    private String accessKeySecret;

    private String endpoint;

    private String bucketName;

    private OSS ossClient;

    public AliyunCloudStorageService() {
        super();
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    protected void doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
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

}
