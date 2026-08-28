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

package io.github.xhrg.util;

import io.github.xhrg.model.AlertLinkMsg;
import io.github.xhrg.model.StatusType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TemplateRendererTest {

    @Autowired
    private TemplateRenderer templateRenderer;

    @Test
    public void testRenderDefaultTemplate() {
        // Prepare test data
        AlertLinkMsg msg = new AlertLinkMsg();
        msg.setName("Test Alert");
        msg.setStatus(StatusType.FIRING);
        msg.setStartTime(new Date());

        LinkedHashMap<String, String> rootAttr = new LinkedHashMap<>();
        rootAttr.put("instance", "localhost:9090");
        rootAttr.put("dashboard", "http://grafana.example.com/d/123");
        msg.setFlatAttrs(rootAttr);

        LinkedHashMap<String, LinkedHashMap<String, String>> groupAttr = new LinkedHashMap<>();
        LinkedHashMap<String, String> group1 = new LinkedHashMap<>();
        group1.put("cpu", "90%");
        group1.put("memory", "85%");
        groupAttr.put("System Metrics", group1);
        msg.setGroupAttrs(groupAttr);

        // Test rendering
        Map<String, Object> variables1 = new HashMap<>();
        variables1.put("name", msg.getName());
        variables1.put("status", msg.getStatus().name());
        variables1.put("startTime", msg.getStartTime());
        variables1.put("endTime", msg.getEndTime());
        variables1.put("flatAttrs", msg.getFlatAttrs());
        variables1.put("groupAttrs", msg.getGroupAttrs());

        String result = templateRenderer.render("dd-group", variables1);

        assertNotNull(result);
        assertTrue(result.contains("Test Alert"));
        assertTrue(result.contains("localhost:9090"));
        assertTrue(result.contains("[dashboard]"));
        assertTrue(result.contains("System Metrics"));
        assertTrue(result.contains("cpu"));
        assertTrue(result.contains("memory"));
    }

    @Test
    public void testRenderFiringTemplate() {
        Map<String, Object> variables2 = new HashMap<>();
        variables2.put("name", "CPU Alert");
        variables2.put("status", StatusType.FIRING.name());
        variables2.put("startTime", new Date());
        variables2.put("flatAttrs", new LinkedHashMap<String, String>());
        variables2.put("groupAttrs", new LinkedHashMap<String, LinkedHashMap<String, String>>());

        String result = templateRenderer.render("dd-group", variables2);

        assertNotNull(result);
        assertTrue(result.contains("🔴"));
        assertTrue(result.contains("告警触发"));
        assertTrue(result.contains("FIRING"));
    }

    @Test
    public void testRenderResolvedTemplate() {
        Map<String, Object> variables3 = new HashMap<>();
        variables3.put("name", "CPU Alert");
        variables3.put("status", StatusType.RESOLVED.name());
        variables3.put("startTime", new Date());
        variables3.put("endTime", new Date());
        variables3.put("flatAttrs", new LinkedHashMap<String, String>());
        variables3.put("groupAttrs", new LinkedHashMap<String, LinkedHashMap<String, String>>());

        String result = templateRenderer.render("dd-group", variables3);

        assertNotNull(result);
        assertTrue(result.contains("✅"));
        assertTrue(result.contains("告警恢复"));
        assertTrue(result.contains("RESOLVED"));
    }
}
