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
package com.youkol.support.storage.oss.qiniu;

import java.util.Objects;

import com.qiniu.storage.Region;

/**
 * 七牛Region封装, 通过定义的名称直接获取对应的Region
 * 对应关系如下：
 * <pre>
 * z0      : 华东机房
 * z1      : 华北机房
 * z2      : 华南机房
 * na0     : 北美机房
 * as0     : 东南亚/新加坡机房
 * qvm-z0  : 华东机房内网
 * qvm-z1  : 华北机房
 * auto    : region名称为空，或者为auto时，即为不指定，均为autoRegion
 * </pre>
 *
 * @author jackiea
 */
public class RegionBuilder {

    private Region region;

    public RegionBuilder() {
        region = Region.autoRegion();
    }

    public RegionBuilder region(String regionName) {
        this.region = Regions.getRegion(regionName);
        return this;
    }

    public Region build() {
        return region;
    }

    enum Regions {
        HUA_DONG("z0", Region.huadong()),
        HUA_BEI("z1", Region.huabei()),
        HUA_NAN("z2", Region.huanan()),
        BEI_MEI("na0", Region.beimei()),
        DONG_NAN_YA("as0", Region.xinjiapo()),
        QVM_HUA_DONG("qvm-z0", Region.qvmHuadong()),
        QVM_HUB_BEI("qvm-z1", Region.qvmHuabei()),
        AUTO("auto", Region.autoRegion());

        private String regionName;

        private Region region;

        Regions(String regionName, Region region) {
            this.regionName = regionName;
            this.region = region;
        }

        public String getRegionName() {
            return regionName;
        }

        public Region getRegion() {
            return region;
        }

        public static Region getRegion(String regionName) {
            for (Regions region : Regions.values()) {
                if (Objects.equals(region.getRegionName(), regionName)) {
                    return region.getRegion();
                }
            }

            return Regions.AUTO.getRegion();
        }
    }

}
