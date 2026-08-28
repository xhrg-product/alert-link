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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.xhrg.dto.prometheus.PrometheusAlertMessage;
import io.github.xhrg.dto.prometheus.PrometheusAlertMessage.Alert;
import io.github.xhrg.model.AlertLinkMsg;
import io.github.xhrg.model.StatusType;
import io.github.xhrg.pipline.AlertMsgPipline;
import io.github.xhrg.util.AlertMsgUtils;
import io.github.xhrg.util.JsonUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Prometheus告警接收器 处理Prometheus Alertmanager发送的webhook告警
 */
@Component
@RestController
public class PrometheusAlertReceiver {

    @Autowired
    private AlertMsgPipline alertMsgPipline;

    @RequestMapping("/for-alertmanager")
    @ResponseBody
    public String alertmanager(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) {
        PrometheusAlertMessage prometheusAlertMessage = JsonUtils.fromJson(body,
                io.github.xhrg.dto.prometheus.PrometheusAlertMessage.class);

        List<Alert> alertList = prometheusAlertMessage.getAlerts();
        for (Alert alert : alertList) {
            AlertLinkMsg msg = new AlertLinkMsg();
            msg.setRawData(alert);
            msg.setHttpRequest(request);
            msg.setName(alert.getLabels().get("alertname"));

            msg.setStatus("firing".equals(alert.getStatus()) ? StatusType.FIRING : StatusType.RESOLVED);
            msg.setCode(AlertMsgUtils.code(alert));
            msg.setStartTime(alert.getStartsAt());
            msg.setEndTime(alert.getEndsAt());

            msg.getGroupAttrs().put("annotations", alert.getAnnotations());
            msg.getGroupAttrs().put("labels", alert.getLabels());

            Map<String, String> rootAttr = msg.getFlatAttrs();
            rootAttr.put("url", alert.getGeneratorURL());

            msg.setStartTime(alert.getStartsAt());
            msg.setEndTime(alert.getEndsAt());

            rootAttr.put("status", alert.getStatus());

            LinkedHashMap<String, String> labels = alert.getLabels();
            msg.getRouteAttrs().putAll(labels);

            alertMsgPipline.in(msg);
        }
        return "OK";
    }
}
