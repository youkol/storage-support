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

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author jackiea
 */
class StorageTypeHelper {
    private static final Map<StorageType, Class<?>> MAPPINGS;

    static {
        Map<StorageType, Class<?>> mappings = new EnumMap<>(StorageType.class);
        mappings.put(StorageType.LOCAL, LocalDiskStorageConfiguration.class);
        mappings.put(StorageType.ALIYUN, AliyunCloudStorageConfiguration.class);
        mappings.put(StorageType.TENCENT, TencentCloudStorageConfiguration.class);
        mappings.put(StorageType.BAIDU, BaiduCloudStorageConfiguration.class);
        mappings.put(StorageType.QINIU, QiniuCloudStorageConfiguration.class);
        mappings.put(StorageType.MINIO, MinioStorageConfiguration.class);

        MAPPINGS = Collections.unmodifiableMap(mappings);
    }

    private StorageTypeHelper() {
    }

    public static String getConfigurationClass(StorageType storageType) {
        Class<?> clazz = MAPPINGS.get(storageType);
        if (clazz == null) {
            throw new IllegalStateException("Unknown storage type: " + storageType);
        }

        return clazz.getName();
    }

    public static StorageType getType(String classname) {
        for (Map.Entry<StorageType, Class<?>> entry : MAPPINGS.entrySet()) {
            if (entry.getValue().getName().equals(classname)) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("Unknown configuration class " + classname);
    }
}
