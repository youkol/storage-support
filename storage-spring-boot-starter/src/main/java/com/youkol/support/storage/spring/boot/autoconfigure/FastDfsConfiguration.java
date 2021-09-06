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
package com.youkol.support.storage.spring.boot.autoconfigure;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.github.tobato.fastdfs.service.TrackerClient;
import com.youkol.support.storage.StorageService;
import com.youkol.support.storage.fastdfs.FastDfsStorageConfig;
import com.youkol.support.storage.fastdfs.FastDfsStorageService;
import com.youkol.support.storage.spring.boot.autoconfigure.StorageProperties.FastDfs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnClass({ TrackerClient.class, FastFileStorageClient.class })
@ConditionalOnMissingBean(StorageService.class)
@Conditional({ StorageCondition.class })
public class FastDfsConfiguration {

    @Bean
    @ConditionalOnBean(FastFileStorageClient.class)
    public FastDfsStorageService storageService(StorageProperties storageProperties, FastFileStorageClient storageClient) {
        FastDfs fastdfs = storageProperties.getFastdfs();
        FastDfsStorageConfig storageConfig = new FastDfsStorageConfig();
        storageConfig.setDomain(fastdfs.getDomain());

        return new FastDfsStorageService(storageConfig, storageClient);
    }
}
