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
package com.youkol.support.storage.fastdfs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.StorageException;

import org.springframework.util.StringUtils;

/**
 * FastDFS
 *
 * @see <a href="https://github.com/happyfish100/fastdfs">Github: FastDFS</a>
 * @see <a href="https://github.com/happyfish100/fastdfs-client-java">Github: fastdfs-client-java</a>
 * @see <a href="https://github.com/tobato/FastDFS_Client">Github: FastDFS_Client</a>
 *
 * @author jackiea
 */
public class FastDfsStorageService extends AbstractStorageService<FastDfsStorageConfig> {

    private FastFileStorageClient storageClient;

    public FastDfsStorageService(FastDfsStorageConfig storageConfig) {
        super(storageConfig);
    }

    public FastDfsStorageService(FastDfsStorageConfig storageConfig, FastFileStorageClient storageClient) {
        super(storageConfig);
        this.storageClient = storageClient;
    }

    @Override
    protected String doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
            String fileExtName = StringUtils.getFilenameExtension(key);
            long fileSize = inputStream.available();

            StorePath storePath = storageClient.uploadFile(inputStream, fileSize, fileExtName, null);
            return storePath.getFullPath();
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    protected String doPutObject(String key, byte[] content) throws StorageException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content)) {
            String fileExtName = StringUtils.getFilenameExtension(key);
            long fileSize = content.length;

            StorePath storePath = storageClient.uploadFile(bais, fileSize, fileExtName, null);
            return storePath.getFullPath();
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

}
