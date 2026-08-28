/*
 * Copyright 2026 xhrg
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

package io.github.xhrg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 告警规则配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "alert")
public class AlertRuleConfig {

    /**
     * 告警规则列表（直接绑定 YAML 列表）
     */
    private List<RuleConfig> rules = new java.util.ArrayList<>();

    @Data
    public static class RuleConfig {
        /**
         * 匹配标签：告警中带有这个标签的会匹配到这个规则
         */
        private Map<String, String> matchTag;

        /**
         * 系统标签：匹配后会把这些标签插入到告警中
         */
        private Map<String, String> insertTags;

        // 便捷方法
        public boolean isEnabled() {
            String enabled = insertTags != null ? insertTags.get("sys_enabled") : null;
            return Boolean.parseBoolean(enabled != null ? enabled : "true");
        }

        public String getDdGroup() {
            return insertTags != null ? insertTags.get("sys_dd_group") : null;
        }
    }
}
