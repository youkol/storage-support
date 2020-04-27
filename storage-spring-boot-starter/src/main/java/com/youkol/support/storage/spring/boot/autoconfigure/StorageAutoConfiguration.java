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
import com.youkol.support.storage.spring.boot.autoconfigure.StorageAutoConfiguration.StorageConfigurationImportSelector;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

/**
 * 参照CacheAutoConfiguration实现此功能
 *
 * @author jackiea
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(StorageService.class)
@EnableConfigurationProperties(StorageProperties.class)
@Import(StorageConfigurationImportSelector.class)
public class StorageAutoConfiguration {

    @Bean
    public StorageServiceValidator storageAutoConfigurationValidator(StorageProperties storageProperties, ObjectProvider<StorageService> storageService) {
        return new StorageServiceValidator(storageProperties, storageService);
    }

    static class StorageServiceValidator implements InitializingBean {

        private final StorageProperties storageProperties;

        private final ObjectProvider<StorageService> storageService;

        public StorageServiceValidator(StorageProperties storageProperties, ObjectProvider<StorageService> storageService) {
            this.storageProperties = storageProperties;
            this.storageService = storageService;
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            Assert.notNull(this.storageService.getIfAvailable(),
                () -> "No Storage Service could be auto-configured, check your configuration (storage type is '"
                + this.storageProperties.getType() + "')");
        }
    }

    static class StorageConfigurationImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            StorageType[] types = StorageType.values();
            String[] result = new String[types.length];
            for (int i = 0; i < types.length; i++) {
                result[i] = StorageTypeHelper.getConfigurationClass(types[i]);
            }

            return result;
        }

    }
}
