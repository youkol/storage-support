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
package com.youkol.support.storage.oss.minio;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.StorageException;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

/**
 * Minio 对象存储
 *
 * @author jackiea
 * @see <a href= "https://docs.min.io/docs/java-client-api-reference.html">Java
 *      Client快速入门指南</a>
 */
public class MinioStorageService extends AbstractStorageService<MinioStorageConfig> {

    private MinioClient minioClient;

    public MinioStorageService(MinioStorageConfig storageConfig) throws StorageException {
        super(storageConfig);
        this.init();
    }

    protected void init() throws StorageException {
        String endpoint = this.getStorageConfig().getEndpoint();
        String accessKey = this.getStorageConfig().getAccessKey();
        String secretKey = this.getStorageConfig().getSecretKey();
        String bucketName = this.getStorageConfig().getBucketName();

        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        this.createBucket(bucketName);
    }

    @Override
    protected String doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(this.getStorageConfig().getBucketName())
                    .object(key)
                    .stream(inputStream, 0, -1)
                    .build();

            this.minioClient.putObject(putObjectArgs);

            return key;
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    protected String doPutObject(String key, byte[] content) throws StorageException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content)) {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(this.getStorageConfig().getBucketName())
                    .object(key)
                    .stream(bais, bais.available(), -1)
                    .build();

            this.minioClient.putObject(putObjectArgs);

            return key;
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    /**
     * Make bucket if not exist.
     *
     * @param bucketName the bucket name
     * @throws StorageException minio bucket check exist or create exception
     */
    private void createBucket(String bucketName) throws StorageException {
        try {
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
            boolean found = this.minioClient.bucketExists(bucketExistsArgs);
            if (found) {
                return;
            }

            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();
            this.minioClient.makeBucket(makeBucketArgs);
        } catch (Exception ex) {
            throw new StorageException("Minio bucket check exist or create exception", ex);
        }
    }

}
