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
import io.github.xhrg.pipline.impl.SkipHandler;
import io.github.xhrg.pipline.impl.SmsHandler;
import io.github.xhrg.pipline.impl.TagsHandler;
import jakarta.annotation.PostConstruct;

@Component
public class AlertMsgPipline {

    private List<AlertMsgHandler> alertMsgHandlerList = new ArrayList<AlertMsgHandler>();

    @Autowired
    private TagsHandler tagsHandler;

    @Autowired
    private DingGroupHandler dingGroupHandler;

    @Autowired
    private SmsHandler smsHandler;

    @Autowired
    private SkipHandler skipHandler;

    // 从上到下进行数据解析处理，顺序严格定义。
    @PostConstruct
    public void init() {
        alertMsgHandlerList.add(tagsHandler);
        alertMsgHandlerList.add(skipHandler);
        alertMsgHandlerList.add(dingGroupHandler);
        alertMsgHandlerList.add(smsHandler);
    }

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