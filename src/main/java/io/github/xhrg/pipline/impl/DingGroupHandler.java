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

package io.github.xhrg.pipline.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.xhrg.constant.SystemTags;
import io.github.xhrg.model.AlertLinkMsg;
import io.github.xhrg.model.StatusType;
import io.github.xhrg.pipline.AlertMsgHandler;
import io.github.xhrg.sender.DingGroupSender;
import io.github.xhrg.util.StrUtils;
import io.github.xhrg.util.TemplateRenderer;

@Component
public class DingGroupHandler implements AlertMsgHandler {

    @Autowired
    private DingGroupSender dingGroupSender;

    @Autowired
    private TemplateRenderer templateRenderer;

    @Override
    public boolean in(AlertLinkMsg alertLinkMsg) {
        String ding = alertLinkMsg.getRouteAttrs().get(SystemTags.DingGroup);
        if (StrUtils.isEmpty(ding)) {
            return true;
        }

        // Prepare template variables (ensure non-null values)
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", alertLinkMsg.getName() != null ? alertLinkMsg.getName() : "");
        variables.put("status", alertLinkMsg.getStatus() != null ? alertLinkMsg.getStatus().name() : StatusType.FIRING.name());
        variables.put("startTime", alertLinkMsg.getStartTime() != null ? alertLinkMsg.getStartTime() : new java.util.Date(0));
        variables.put("endTime", alertLinkMsg.getEndTime() != null ? alertLinkMsg.getEndTime() : new java.util.Date(0));
        variables.put("flatAttrs", alertLinkMsg.getFlatAttrs() != null ? alertLinkMsg.getFlatAttrs() : new LinkedHashMap<>());
        variables.put("groupAttrs", alertLinkMsg.getGroupAttrs() != null ? alertLinkMsg.getGroupAttrs() : new LinkedHashMap<>());

        // Render template (template handles firing/resolved logic internally)
        String text = templateRenderer.render("dd-group", variables);

        List<String> listPhone = new ArrayList<String>();
        dingGroupSender.doSendDingding(ding, text, listPhone);
        return true;
    }
}