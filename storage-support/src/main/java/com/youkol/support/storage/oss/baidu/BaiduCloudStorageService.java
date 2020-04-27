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
package com.youkol.support.storage.oss.baidu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.baidubce.auth.BceCredentials;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.PutObjectRequest;
import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.StorageException;

/**
 * 百度云对象存储
 *
 * @see <a href="https://cloud.baidu.com/doc/BOS/s/Fjwvyrqw2">百度云对象存储开发文档</a>
 *
 * @author jackiea
 */
public class BaiduCloudStorageService extends AbstractStorageService<BaiduCloudStorageConfig> {

    private BosClient bosClient;

    public BaiduCloudStorageService(BaiduCloudStorageConfig storageConfig) {
        super(storageConfig);
        this.init();
    }

    protected void init() {
        String endpoint = this.getStorageConfig().getEndpoint();
        String accessKeyId = this.getStorageConfig().getAccessKeyId();
        String accessKeySecret = this.getStorageConfig().getAccessKeySecret();

        BceCredentials credentials = new DefaultBceCredentials(accessKeyId, accessKeySecret);
        BosClientConfiguration bosClientConfiguration = new BosClientConfiguration()
            .withCredentials(credentials)
            .withEndpoint(endpoint);

        this.bosClient = new BosClient(bosClientConfiguration);
    }

    @Override
    protected void doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
            String bucketName = this.getStorageConfig().getBucketName();
            PutObjectRequest request = new PutObjectRequest(bucketName, key, inputStream);
            bosClient.putObject(request);
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    protected void doPutObject(String key, byte[] content) throws StorageException {
        putObject(key, new ByteArrayInputStream(content));
    }

    public BosClient getBosClient() {
        return bosClient;
    }
}
