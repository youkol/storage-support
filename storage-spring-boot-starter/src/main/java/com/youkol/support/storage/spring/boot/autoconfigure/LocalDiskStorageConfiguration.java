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

import com.youkol.support.storage.StorageService;
import com.youkol.support.storage.local.LocalDiskStorageConfig;
import com.youkol.support.storage.local.LocalDiskStorageService;
import com.youkol.support.storage.spring.boot.autoconfigure.LocalDiskStorageConfiguration.LocalAvailableCondition;
import com.youkol.support.storage.spring.boot.autoconfigure.LocalDiskStorageConfiguration.LocalDiskWebMvcConfigurer;
import com.youkol.support.storage.spring.boot.autoconfigure.StorageProperties.LocalDisk;

import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author jackiea
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ LocalDiskStorageService.class })
@ConditionalOnMissingBean(StorageService.class)
@Conditional({ StorageCondition.class, LocalAvailableCondition.class })
@Import(LocalDiskWebMvcConfigurer.class)
class LocalDiskStorageConfiguration {

    @Bean
    public LocalDiskStorageService storageService(StorageProperties storageProperties) {
        LocalDiskStorageConfig storageConfig = new LocalDiskStorageConfig();

        LocalDisk localDisk = storageProperties.getLocal();
        storageConfig.setDomain(localDisk.getDomain());
        storageConfig.setUploadLocation(localDisk.getUploadLocation());
        storageConfig.setContextPath(localDisk.getContextPath());

        return new LocalDiskStorageService(storageConfig);
    }

    static class LocalAvailableCondition extends SpringBootCondition {

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            ConditionMessage.Builder message = ConditionMessage.forCondition("Local");
            String uploadLocationProperty = "youkol.storage.local.upload-location";
            Environment environment = context.getEnvironment();
            if (environment.containsProperty(uploadLocationProperty)) {
                return ConditionOutcome.match(message.because("uploadLocation property exists"));
            }

            return ConditionOutcome.noMatch(message.because("has not been set"));
        }

    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = Type.SERVLET)
    public static class LocalDiskWebMvcConfigurer implements WebMvcConfigurer {

        private StorageProperties storageProperties;

        public LocalDiskWebMvcConfigurer(StorageProperties storageProperties) {
            this.storageProperties = storageProperties;
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            LocalDisk localDisk = this.storageProperties.getLocal();
            registry.addResourceHandler(localDisk.getContextPath() + "/**")
                    .addResourceLocations("file:" + localDisk.getUploadLocation() + "/");
        }
    }
}
