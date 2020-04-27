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

/**
 * 对象存储服务接口
 *
 * @author jackiea
 */
public interface StorageService {

    /**
     * 上传
     *
     * @param key  对象名称
     *             上传文件到OSS时需要指定包含文件后缀在内的完整路径，
     *             例如abc/efg/123.jpg。
     * @param inputStream 文件/对象数据流
     * @return 返回对象/文件的访问地址
     * @throws StorageException 存储异常信息
     */
    String putObject(String key, InputStream inputStream) throws StorageException;

    /**
     * 上传
     *
     * @param key  对象名称
     *             上传文件到OSS时需要指定包含文件后缀在内的完整路径，
     *             例如abc/efg/123.jpg。
     * @param content 文件/对象的字节数组
     * @return 返回对象/文件的访问地址
     * @throws StorageException 存储异常信息
     */
    String putObject(String key, byte[] content) throws StorageException;
}
