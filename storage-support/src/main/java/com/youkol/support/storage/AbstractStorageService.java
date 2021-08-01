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
package com.youkol.support.storage;

import java.io.InputStream;

import com.google.common.base.Strings;

/**
 * 对象存储服务抽象类实现
 *
 * @author jackiea
 */
public abstract class AbstractStorageService<T extends StorageConfig> implements StorageService {

    protected final T storageConfig;

    protected AbstractStorageService(T storageConfig) {
        this.storageConfig = storageConfig;
    }

    public String putObject(String key, InputStream inputStream) throws StorageException {
        this.doPutObject(key, inputStream);
        return this.getObjectUrl(key);
    }

    public String putObject(String key, byte[] content) throws StorageException {
        this.doPutObject(key, content);
        return this.getObjectUrl(key);
    }

    protected abstract void doPutObject(String key, InputStream inputStream) throws StorageException;

    protected abstract void doPutObject(String key, byte[] content) throws StorageException;

    /**
     * 获取存储对象的访问地址
     *
     * @param key  对象名称
     *             上传文件到OSS时需要指定包含文件后缀在内的完整路径，
     *             例如abc/efg/123.jpg。
     * @return 返回对象对应的访问地址
     */
    protected String getObjectUrl(String key) {
        String result = Strings.nullToEmpty(key);
        String domain = storageConfig.getDomain();
        if (!Strings.isNullOrEmpty(domain)) {
            result = domain + "/" + key;
        }

        return Strings.emptyToNull(result);
    }

    public T getStorageConfig() {
        return storageConfig;
    }

}
