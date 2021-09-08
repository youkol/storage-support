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
package com.youkol.support.storage.local;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.youkol.support.storage.AbstractStorageService;
import com.youkol.support.storage.StorageException;

import org.springframework.util.StringUtils;

/**
 * 本地磁盘存储服务 <br>
 * 示例：
 *
 * <pre>
 * LocalDiskStorageConfig localConfig = new LocalDiskStorageConfig();
 * StorageService service = new LocalDiskStorageService(localConfig);
 * String fileName = "C:\\1111.jpg";
 * FileInputStream inputStream = new FileInputStream(new File(fileName));
 * String url = service.putObject("image/2018-11/13/10101.jpg", inputStream);
 * </pre>
 *
 * @author jackiea
 */
public class LocalDiskStorageService extends AbstractStorageService<LocalDiskStorageConfig> {

    public LocalDiskStorageService(LocalDiskStorageConfig storageConfig) {
        super(storageConfig);
    }

    @Override
    protected String doPutObject(String key, InputStream inputStream) throws StorageException {
        try {
            LocalDiskStorageConfig localConfig = this.getStorageConfig();
            Path targetPath = Paths.get(localConfig.getUploadLocation(), key);
            if (targetPath.getParent() != null) {
                Files.createDirectories(targetPath.getParent());
            }

            Files.copy(inputStream, targetPath);

            String contextPath = localConfig.getContextPath();
            return StringUtils.hasText(contextPath) ? contextPath + "/" + key : key;
        } catch (Exception ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    protected String doPutObject(String key, byte[] content) throws StorageException {
        return doPutObject(key, new ByteArrayInputStream(content));
    }

}
