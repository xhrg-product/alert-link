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
import java.util.LinkedHashMap;

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
        String result = templateRenderer.render("dd-group",
            java.util.Map.of(
                "name", msg.getName(),
                "status", msg.getStatus().name(),
                "startTime", msg.getStartTime(),
                "endTime", msg.getEndTime(),
                "flatAttrs", msg.getFlatAttrs(),
                "groupAttrs", msg.getGroupAttrs()
            ));

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
        String result = templateRenderer.render("dd-group",
            java.util.Map.of(
                "name", "CPU Alert",
                "status", StatusType.FIRING.name(),
                "startTime", new Date(),
                "flatAttrs", new LinkedHashMap<String, String>(),
                "groupAttrs", new LinkedHashMap<String, LinkedHashMap<String, String>>()
            ));

        assertNotNull(result);
        assertTrue(result.contains("🔴"));
        assertTrue(result.contains("告警触发"));
        assertTrue(result.contains("FIRING"));
    }

    @Test
    public void testRenderResolvedTemplate() {
        String result = templateRenderer.render("dd-group",
            java.util.Map.of(
                "name", "CPU Alert",
                "status", StatusType.RESOLVED.name(),
                "startTime", new Date(),
                "endTime", new Date(),
                "flatAttrs", new LinkedHashMap<String, String>(),
                "groupAttrs", new LinkedHashMap<String, LinkedHashMap<String, String>>()
            ));

        assertNotNull(result);
        assertTrue(result.contains("✅"));
        assertTrue(result.contains("告警恢复"));
        assertTrue(result.contains("RESOLVED"));
    }
}
