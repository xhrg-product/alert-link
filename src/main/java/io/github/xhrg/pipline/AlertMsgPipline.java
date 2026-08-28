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

package io.github.xhrg.pipline;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.xhrg.model.AlertLinkMsg;
import io.github.xhrg.pipline.impl.DingGroupHandler;
import io.github.xhrg.pipline.impl.SilenceHandler;
import io.github.xhrg.pipline.impl.SkipHandler;
import io.github.xhrg.pipline.impl.SmsHandler;
import io.github.xhrg.pipline.impl.TagsHandler;
import javax.annotation.PostConstruct;

/**
 * 告警消息处理管道
 * <p>
 * 负责按顺序执行一系列告警消息处理器，包括标签处理、跳过处理、静默处理、
 * 钉钉群通知处理和短信通知处理等。
 * </p>
 */
@Component
public class AlertMsgPipline {

    /**
     * 告警消息处理器列表
     * 按初始化顺序依次执行
     */
    private List<AlertMsgHandler> alertMsgHandlerList = new ArrayList<AlertMsgHandler>();

    /**
     * 标签处理器
     */
    @Autowired
    private TagsHandler tagsHandler;

    /**
     * 钉钉群通知处理器
     */
    @Autowired
    private DingGroupHandler dingGroupHandler;

    /**
     * 短信通知处理器
     */
    @Autowired
    private SmsHandler smsHandler;

    /**
     * 跳过处理器
     */
    @Autowired
    private SkipHandler skipHandler;

    /**
     * 静默处理器
     */
    @Autowired
    private SilenceHandler silenceHandler;

    /**
     * 初始化处理器链
     * <p>
     * 按照严格的顺序注册处理器，处理顺序为：
     * 1. 标签处理 - 解析和添加标签信息
     * 2. 跳过处理 - 判断是否需要跳过该告警
     * 3. 静默处理 - 判断是否在静默期内
     * 4. 钉钉群通知 - 发送钉钉群消息
     * 5. 短信通知 - 发送短信通知
     * </p>
     */
    @PostConstruct
    public void init() {
        alertMsgHandlerList.add(tagsHandler);
        alertMsgHandlerList.add(skipHandler);
        alertMsgHandlerList.add(silenceHandler);
        alertMsgHandlerList.add(dingGroupHandler);
        alertMsgHandlerList.add(smsHandler);
    }

    /**
     * 执行告警消息处理管道
     * <p>
     * 按顺序执行所有处理器，如果任一处理器返回 false，则中断处理并返回 false。
     * 所有处理器都返回 true 时，才认为该告警消息处理成功。
     * </p>
     *
     * @param alertLinkMsg 告警链接消息
     * @return 所有处理器都通过时返回 true，否则返回 false
     */
    public boolean in(AlertLinkMsg alertLinkMsg) {
        for (AlertMsgHandler alertMsgHandler : alertMsgHandlerList) {
            boolean ok = alertMsgHandler.in(alertLinkMsg);
            if (!ok) {
                return false;
            }
        }
        return true;
    }
}