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
package com.youkol.support.storage.spring.boot.autoconfigure;

import com.aliyun.oss.OSS;
import com.youkol.support.storage.StorageService;
import com.youkol.support.storage.oss.aliyun.AliyunCloudStorageConfig;
import com.youkol.support.storage.oss.aliyun.AliyunCloudStorageService;
import com.youkol.support.storage.spring.boot.autoconfigure.StorageProperties.Aliyun;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author jackiea
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ OSS.class, AliyunCloudStorageService.class })
@ConditionalOnMissingBean(StorageService.class)
@Conditional(StorageCondition.class)
class AliyunCloudStorageConfiguration {

    @Bean
    public AliyunCloudStorageService storageService(StorageProperties storageProperties) {
        AliyunCloudStorageConfig storageConfig = new AliyunCloudStorageConfig();

        Aliyun aliyun = storageProperties.getAliyun();
        storageConfig.setDomain(aliyun.getDomain());
        storageConfig.setAccessKeyId(aliyun.getAccessKeyId());
        storageConfig.setAccessKeySecret(aliyun.getAccessKeySecret());
        storageConfig.setBucketName(aliyun.getBucketName());
        storageConfig.setEndpoint(aliyun.getEndpoint());

        AliyunCloudStorageService storageService = new AliyunCloudStorageService(storageConfig);
        return storageService;
    }
}