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

package io.github.xhrg.constant;

/**
 * 系统标签常量 用于区分系统配置标签和业务标签 系统标签以 sys_ 为前缀
 */
public class SystemTags {

    // 钉钉群地址
    public static final String DingGroup = "sys_dd_group";

    // 是否跳过
    public static final String SKIP = "sys_skip";

    // 静默，单位分钟。
    public static final String SILENCE = "sys_silence";

    /**
     * 系统标签前缀
     */
    public static final String SYS_PREFIX = "sys_";

    private SystemTags() {
        // 防止实例化
    }
}
