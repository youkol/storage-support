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

import com.youkol.support.storage.StorageService;
import com.youkol.support.storage.local.LocalDiskStorageConfig;
import com.youkol.support.storage.local.LocalDiskStorageService;
import com.youkol.support.storage.spring.boot.autoconfigure.StorageProperties.LocalDisk;

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
@ConditionalOnClass({ LocalDiskStorageService.class })
@ConditionalOnMissingBean(StorageService.class)
@Conditional(StorageCondition.class)
class LocalDiskStorageConfiguration {

    @Bean
    public LocalDiskStorageService storageService(StorageProperties storageProperties) {
        LocalDiskStorageConfig storageConfig = new LocalDiskStorageConfig();

        LocalDisk localDisk = storageProperties.getLocal();
        storageConfig.setDomain(localDisk.getDomain());
        storageConfig.setUploadLocation(localDisk.getUploadLocation());

        LocalDiskStorageService storageService = new LocalDiskStorageService(storageConfig);
        return storageService;
    }
}
