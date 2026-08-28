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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * Thymeleaf template renderer for notification messages
 */
@Component
public class TemplateRenderer {

    private static final Logger logger = LoggerFactory.getLogger(TemplateRenderer.class);

    private final TemplateEngine templateEngine;

    public TemplateRenderer(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Render a template with the given context
     *
     * @param templateName the template name (e.g., "notification/dingtalk/alert-default")
     * @param variables    the variables to be used in the template
     * @return the rendered template content
     */
    public String render(String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);
            String result = templateEngine.process(templateName, context);
            // Clean up extra blank lines from Thymeleaf processing
            return cleanTemplateOutput(result);
        } catch (Exception e) {
            logger.error("Failed to render template: {}", templateName, e);
            throw new RuntimeException("Template rendering failed", e);
        }
    }

    /**
     * Clean up the template output by removing excessive blank lines
     */
    private String cleanTemplateOutput(String content) {
        // Replace multiple consecutive blank lines with a single blank line
        return content.replaceAll("\n{3,}", "\n\n").trim();
    }
}
