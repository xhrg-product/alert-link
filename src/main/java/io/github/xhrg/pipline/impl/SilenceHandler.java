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

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import io.github.xhrg.constant.SystemTags;
import io.github.xhrg.model.AlertLinkMsg;
import io.github.xhrg.pipline.AlertMsgHandler;
import io.github.xhrg.util.StrUtils;

@Component
public class SilenceHandler implements AlertMsgHandler {

    private Cache<Long, Long> cache = Caffeine.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).maximumSize(10_000)
            .build();

    @Override
    public boolean in(AlertLinkMsg alertLinkMsg) {
        String min = alertLinkMsg.getRouteAttrs().get(SystemTags.SILENCE);
        if (StrUtils.isEmpty(min)) {
            return true;
        }

        int silence = parse(min);
        if (silence <= 0) {
            return true;
        }

        long code = alertLinkMsg.getCode();
        Long lastTime = cache.getIfPresent(code);
        if (lastTime == null) {
            cache.put(code, System.currentTimeMillis());
            return true;
        }

        long dur = (System.currentTimeMillis() - lastTime) / 1000 / 60;

        if (dur < silence) {
            return false;
        }

        return true;
    }

    private int parse(String min) {
        try {
            int i = Integer.parseInt(min);
            return i;
        } catch (Exception e) {
            return 0;
        }
    }
}