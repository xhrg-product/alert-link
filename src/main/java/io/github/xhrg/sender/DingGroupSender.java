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

package io.github.xhrg.sender;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;

@Component
public class DingGroupSender {

    private Logger logger = LoggerFactory.getLogger(DingGroupSender.class);

    public void doSendDingding(String url, String message, List<String> atMopbile) {
        if (atMopbile == null) {
            atMopbile = new ArrayList<String>();
        }

        // 手机号码去重, 去重不乱序
        List<String> atMopbileUse = new ArrayList<String>();
        for (String str : atMopbile) {
            if (!atMopbileUse.contains(str)) {
                atMopbileUse.add(str);
            }
        }
        if (message == null || message.isEmpty()) {
            return;
        }
        DingTalkClient client = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");

        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();

        StringBuilder sb = new StringBuilder();
        for (String m : atMopbileUse) {
            sb.append("@" + m + " ");
        }

        markdown.setText(message + "\n" + sb.toString());
        markdown.setTitle("告警通知");

        request.setMarkdown(markdown);

        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(atMopbileUse);
        request.setAt(at);
        try {
            OapiRobotSendResponse response = client.execute(request);
            if (!response.isSuccess()) {
                logger.error("ding talk execute error fail, {}", response.getBody());
            }
        } catch (ApiException e) {
            logger.error("ding talk execute error", e);
        }
    }
}
