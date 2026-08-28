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

package io.github.xhrg.model;

import java.util.Date;

import java.util.LinkedHashMap;

import jakarta.servlet.http.HttpServletRequest;

public class AlertLinkMsg {

    // 原始数据
    private Object rawData;

    // 原始数据
    private HttpServletRequest httpRequest;

    // 告警状态
    private StatusType status;

    // 告警名称
    private String name = "";

    private Date startTime;

    private Date endTime;

    // 唯一性标记
    private long code;

    // 展示参数 - 扁平属性
    private LinkedHashMap<String, String> flatAttrs = new LinkedHashMap<String, String>();

    // 展示参数 - 分组属性
    private LinkedHashMap<String, LinkedHashMap<String, String>> groupAttrs = new LinkedHashMap<String, LinkedHashMap<String, String>>();

    // 控制参数 - 路由属性
    private LinkedHashMap<String, String> routeAttrs = new LinkedHashMap<String, String>();

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getRawData() {
        return rawData;
    }

    public void setRawData(Object rawData) {
        this.rawData = rawData;
    }

    public LinkedHashMap<String, String> getFlatAttrs() {
        return flatAttrs;
    }

    public void setFlatAttrs(LinkedHashMap<String, String> flatAttrs) {
        this.flatAttrs = flatAttrs;
    }

    public LinkedHashMap<String, LinkedHashMap<String, String>> getGroupAttrs() {
        return groupAttrs;
    }

    public void setGroupAttrs(LinkedHashMap<String, LinkedHashMap<String, String>> groupAttrs) {
        this.groupAttrs = groupAttrs;
    }

    public LinkedHashMap<String, String> getRouteAttrs() {
        return routeAttrs;
    }

    public void setRouteAttrs(LinkedHashMap<String, String> routeAttrs) {
        this.routeAttrs = routeAttrs;
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

}