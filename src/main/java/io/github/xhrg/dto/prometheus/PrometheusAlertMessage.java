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

package io.github.xhrg.dto.prometheus;

import java.util.Date;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class PrometheusAlertMessage {

    private String receiver;

    private String status;

    private List<Alert> alerts;

    private HashMap<String, Object> groupLabels;

    private HashMap<String, Object> commonLabels;

    private HashMap<String, Object> commonAnnotations;

    private String externalURL;

    private String version;

    private String groupKey;

    private Integer truncatedAlerts;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public HashMap<String, Object> getGroupLabels() {
        return groupLabels;
    }

    public void setGroupLabels(HashMap<String, Object> groupLabels) {
        this.groupLabels = groupLabels;
    }

    public HashMap<String, Object> getCommonLabels() {
        return commonLabels;
    }

    public void setCommonLabels(HashMap<String, Object> commonLabels) {
        this.commonLabels = commonLabels;
    }

    public HashMap<String, Object> getCommonAnnotations() {
        return commonAnnotations;
    }

    public void setCommonAnnotations(HashMap<String, Object> commonAnnotations) {
        this.commonAnnotations = commonAnnotations;
    }

    public String getExternalURL() {
        return externalURL;
    }

    public void setExternalURL(String externalURL) {
        this.externalURL = externalURL;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public Integer getTruncatedAlerts() {
        return truncatedAlerts;
    }

    public void setTruncatedAlerts(Integer truncatedAlerts) {
        this.truncatedAlerts = truncatedAlerts;
    }

    public static class Alert {
        private String status;
        private LinkedHashMap<String, String> labels;
        private LinkedHashMap<String, String> annotations;
        private Date startsAt;
        private Date endsAt;
        private String generatorURL;
        private String fingerprint;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LinkedHashMap<String, String> getLabels() {
            return labels;
        }

        public void setLabels(LinkedHashMap<String, String> labels) {
            this.labels = labels;
        }

        public LinkedHashMap<String, String> getAnnotations() {
            return annotations;
        }

        public void setAnnotations(LinkedHashMap<String, String> annotations) {
            this.annotations = annotations;
        }

        public Date getStartsAt() {
            return startsAt;
        }

        public void setStartsAt(Date startsAt) {
            this.startsAt = startsAt;
        }

        public Date getEndsAt() {
            return endsAt;
        }

        public void setEndsAt(Date endsAt) {
            this.endsAt = endsAt;
        }

        public String getGeneratorURL() {
            return generatorURL;
        }

        public void setGeneratorURL(String generatorURL) {
            this.generatorURL = generatorURL;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }

    }
}
