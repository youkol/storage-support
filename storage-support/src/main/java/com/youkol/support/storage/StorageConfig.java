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
package com.youkol.support.storage;

/**
 * 存储配置信息
 *
 * @author jackiea
 */
public interface StorageConfig {

    /**
     * <p>获取对象访问域名</p>
     * <p>
     * 具体域名请参考各对象存储云服务商，
     * 如果为本地存储，则可设置为自有域名，
     * 以便后续访问时使用
     * </p>
     *
     * @return 对象访问域名
     */
    public String getDomain();
}
