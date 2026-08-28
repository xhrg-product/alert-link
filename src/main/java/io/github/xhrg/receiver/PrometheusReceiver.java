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

package io.github.xhrg.receiver;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.xhrg.dto.prometheus.PrometheusAlertMessage;
import io.github.xhrg.model.AlertLinkMsg;
import io.github.xhrg.model.StatusType;
import io.github.xhrg.pipline.AlertMsgPipline;
import io.github.xhrg.util.AlertMsgUtils;
import io.github.xhrg.util.JsonUtils;
import io.github.xhrg.util.TimeUtils;
import javax.servlet.http.HttpServletRequest;

@Component
@RestController
public class PrometheusReceiver {

    private static Logger logger = LoggerFactory.getLogger(PrometheusReceiver.class);

    // 在一定周期内，恢复通知只发送一次。
    private static final int SILENT_MINUTES = 20;

    private Cache<Long, Boolean> cache = Caffeine.newBuilder().expireAfterWrite(SILENT_MINUTES, TimeUnit.MINUTES)
            .maximumSize(10_000).build();

    @Autowired
    private AlertMsgPipline alertMsgPipline;

    // 该地址固定是Prometheus的配置方法
    @RequestMapping("/api/v2/alerts")
    @ResponseBody
    public String alert(@RequestBody String body, HttpServletRequest request) {

        List<PrometheusAlertMessage.Alert> alertMsgList = null;

        try {
            alertMsgList = JsonUtils.fromArray(body, io.github.xhrg.dto.prometheus.PrometheusAlertMessage.Alert.class);
        } catch (Exception e) {
            logger.error(body);
            return "ERROR";
        }

        for (PrometheusAlertMessage.Alert a : alertMsgList) {
            alert(a, request);
        }

        return "OK";
    }

    private String alert(PrometheusAlertMessage.Alert alertMsg, HttpServletRequest request) {

        logger.info("alert, start is {}, end is {}, body is {}", TimeUtils.timeToLocal(alertMsg.getStartsAt()),
                TimeUtils.timeToLocal(alertMsg.getEndsAt()), JsonUtils.toJson(alertMsg));

        AlertLinkMsg msg = new AlertLinkMsg();
        long msgCode = AlertMsgUtils.code(alertMsg);

        Date date = alertMsg.getEndsAt();
        // 结束时间如果小于当前时间，则表示恢复消息。恢复消息20分钟内只发送一次就行。
        if (date.getTime() < System.currentTimeMillis()) {
            Boolean have = cache.getIfPresent(msgCode);
            if (have != null && have) {
                return "OK";
            }
            cache.put(msgCode, true);
            msg.setStatus(StatusType.RESOLVED);
        } else {
            msg.setStatus(StatusType.FIRING);
        }

        msg.setCode(msgCode);
        msg.setRawData(alertMsg);
        msg.setHttpRequest(request);
        msg.setName(alertMsg.getLabels().get("alertname"));

        msg.getGroupAttrs().put("annotations", alertMsg.getAnnotations());
        msg.getGroupAttrs().put("labels", alertMsg.getLabels());

        Map<String, String> rootAttr = msg.getFlatAttrs();
        rootAttr.put("url", alertMsg.getGeneratorURL());

        msg.setStartTime(alertMsg.getStartsAt());
        msg.setEndTime(alertMsg.getEndsAt());

        LinkedHashMap<String, String> labels = alertMsg.getLabels();
        msg.getRouteAttrs().putAll(labels);

        alertMsgPipline.in(msg);

        return "OK";
    }
}
