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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.xhrg.config.AlertRuleConfig;
import io.github.xhrg.config.AlertRuleConfig.RuleConfig;
import io.github.xhrg.model.AlertLinkMsg;
import io.github.xhrg.pipline.AlertMsgHandler;

@Component
public class TagsHandler implements AlertMsgHandler {

    @Autowired
    private AlertRuleConfig pac;

    @Override
    public boolean in(AlertLinkMsg alertLinkMsg) {

        List<RuleConfig> rules = pac.getRules();
        nextRule: for (RuleConfig rule : rules) {
            Map<String, String> matchTag = rule.getMatchTag();
            for (Map.Entry<String, String> matchEntry : matchTag.entrySet()) {
                boolean ok = matchEntry.getValue().equals(alertLinkMsg.getRouteAttrs().get(matchEntry.getKey()));
                if (!ok) {
                    continue nextRule;
                }
            }
            alertLinkMsg.getRouteAttrs().putAll(rule.getInsertTags());
        }

        return true;
    }
}