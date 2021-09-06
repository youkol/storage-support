/*
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
package com.youkol.support.storage.oss.qiniu;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.ByteStreams;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.StorageException;

/**
 * 七牛云对象存储
 *
 * @see <a href="https://developer.qiniu.com/kodo/sdk/1239/java">七牛云对象存储开发文档</a>
 *
 * @author jackiea
 */
public class QiniuCloudStorageService extends AbstractStorageService<QiniuCloudStorageConfig> {

    private UploadManager uploadManager;

    private String upToken;

    public QiniuCloudStorageService(QiniuCloudStorageConfig storageConfig) {
        super(storageConfig);
        this.init();
    }

    protected void init() {
        String accessKey = this.getStorageConfig().getAccessKey();
        String secretKey = this.getStorageConfig().getSecretKey();
        String regionName = this.getStorageConfig().getRegionName();
        String bucketName = this.getStorageConfig().getBucketName();

        Region region = new RegionBuilder().region(regionName).build();
        Configuration configuration = new Configuration(region);
        this.uploadManager = new UploadManager(configuration);

        Auth auth = Auth.create(accessKey, secretKey);
        this.upToken = auth.uploadToken(bucketName);
    }

    @Override
    protected String doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
            return doPutObject(key, ByteStreams.toByteArray(inputStream));
        } catch (IOException ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    protected String doPutObject(String key, byte[] content) throws StorageException {
        try {
            Response response = uploadManager.put(content, key, upToken);
            if (!response.isOK()) {
                throw new StorageException("Qiniu upload file went wrong: " + response.toString());
            }

            return key;
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    public UploadManager getUploadManager() {
        return uploadManager;
    }

    public String getUpToken() {
        return upToken;
    }

}
