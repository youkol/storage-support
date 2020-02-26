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
package com.youkol.support.storage.qiniu;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.ByteStreams;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.exception.StorageException;

/**
 * 七牛云存储
 *
 * @see https://developer.qiniu.com/kodo/sdk/1239/java
 *
 * @author jackiea
 */
public class QiniuCloudStorageService extends AbstractStorageService {

    private String accessKey;

    private String secretKey;

    private String bucketName;

    private String regionName;

    private UploadManager uploadManager;

    private String upToken;

    public QiniuCloudStorageService() {
        super();
        Region region = new RegionBuilder().region(regionName).build();
        Configuration configuration = new Configuration(region);
        uploadManager = new UploadManager(configuration);

        Auth auth = Auth.create(accessKey, secretKey);
        this.upToken = auth.uploadToken(bucketName);
    }

    @Override
    protected void doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
            putObject(key, ByteStreams.toByteArray(inputStream));
        } catch (IOException ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    protected void doPutObject(String key, byte[] content) throws StorageException {
        try {
            Response response = uploadManager.put(content, key, upToken);
            if (!response.isOK()) {
                throw new StorageException("Qiniu upload file went wrong: " + response.toString());
            }
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

}
