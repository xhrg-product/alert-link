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

import javax.servlet.http.HttpServletRequest;

/**
 * 告警链接消息模型
 * <p>
 * 用于封装告警消息的各项属性，包括原始数据、告警状态、展示参数和路由参数等。
 * 该模型支持扁平属性、分组属性和路由属性三种不同类型的参数展示方式。
 * </p>
 */
public class AlertLinkMsg {

    /**
     * 原始数据对象
     * 存储未经处理的原始告警数据
     */
    private Object rawData;

    /**
     * HTTP 请求对象
     * 存储原始的 HTTP 请求信息
     */
    private HttpServletRequest httpRequest;

    /**
     * 告警状态
     * 表示当前告警的状态类型
     */
    private StatusType status;

    /**
     * 告警名称
     * 告警的简短描述性名称
     */
    private String name = "";

    /**
     * 告警开始时间
     */
    private Date startTime;

    /**
     * 告警结束时间
     */
    private Date endTime;

    /**
     * 唯一性标记
     * 用于标识告警的唯一性，支持去重和匹配
     */
    private long code;

    /**
     * 展示参数 - 扁平属性
     * <p>
     * 以键值对形式存储的扁平化展示属性，按插入顺序保持
     * </p>
     */
    private LinkedHashMap<String, String> flatAttrs = new LinkedHashMap<String, String>();

    /**
     * 展示参数 - 分组属性
     * <p>
     * 以分组形式存储的展示属性，支持多层级的键值对结构
     * </p>
     */
    private LinkedHashMap<String, LinkedHashMap<String, String>> groupAttrs = new LinkedHashMap<String, LinkedHashMap<String, String>>();

    /**
     * 控制参数 - 路由属性
     * <p>
     * 用于控制告警路由和分发策略的属性
     * </p>
     */
    private LinkedHashMap<String, String> routeAttrs = new LinkedHashMap<String, String>();

    /**
     * 获取告警状态
     *
     * @return 告警状态类型
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * 设置告警状态
     *
     * @param status 告警状态类型
     */
    public void setStatus(StatusType status) {
        this.status = status;
    }

    /**
     * 获取告警名称
     *
     * @return 告警名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置告警名称
     *
     * @param name 告警名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取原始数据对象
     *
     * @return 原始数据对象
     */
    public Object getRawData() {
        return rawData;
    }

    /**
     * 设置原始数据对象
     *
     * @param rawData 原始数据对象
     */
    public void setRawData(Object rawData) {
        this.rawData = rawData;
    }

    /**
     * 获取扁平属性映射
     *
     * @return 扁平属性映射
     */
    public LinkedHashMap<String, String> getFlatAttrs() {
        return flatAttrs;
    }

    /**
     * 设置扁平属性映射
     *
     * @param flatAttrs 扁平属性映射
     */
    public void setFlatAttrs(LinkedHashMap<String, String> flatAttrs) {
        this.flatAttrs = flatAttrs;
    }

    /**
     * 获取分组属性映射
     *
     * @return 分组属性映射
     */
    public LinkedHashMap<String, LinkedHashMap<String, String>> getGroupAttrs() {
        return groupAttrs;
    }

    /**
     * 设置分组属性映射
     *
     * @param groupAttrs 分组属性映射
     */
    public void setGroupAttrs(LinkedHashMap<String, LinkedHashMap<String, String>> groupAttrs) {
        this.groupAttrs = groupAttrs;
    }

    /**
     * 获取路由属性映射
     *
     * @return 路由属性映射
     */
    public LinkedHashMap<String, String> getRouteAttrs() {
        return routeAttrs;
    }

    /**
     * 设置路由属性映射
     *
     * @param routeAttrs 路由属性映射
     */
    public void setRouteAttrs(LinkedHashMap<String, String> routeAttrs) {
        this.routeAttrs = routeAttrs;
    }

    /**
     * 获取 HTTP 请求对象
     *
     * @return HTTP 请求对象
     */
    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    /**
     * 设置 HTTP 请求对象
     *
     * @param httpRequest HTTP 请求对象
     */
    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * 获取告警开始时间
     *
     * @return 告警开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置告警开始时间
     *
     * @param startTime 告警开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取告警结束时间
     *
     * @return 告警结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置告警结束时间
     *
     * @param endTime 告警结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取唯一性标记
     *
     * @return 唯一性标记
     */
    public long getCode() {
        return code;
    }

    /**
     * 设置唯一性标记
     *
     * @param code 唯一性标记
     */
    public void setCode(long code) {
        this.code = code;
    }
}