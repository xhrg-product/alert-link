# Alert Link

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)

> 一个轻量级、可扩展的告警处理系统，基于标签驱动实现多监控源的告警接收、处理和分发。

## 产品介绍

Alert Link 是一个基于 Spring Boot 构建的告警处理系统，旨在解决多监控源告警统一处理的问题。系统采用管道式处理模式（Pipeline Pattern），通过标签匹配规则自动为告警追加系统标签，实现灵活的告警路由和分发。

### 核心特性

- **标签驱动** - 基于 Prometheus 的标签设计理念，通过标签匹配实现告警路由
- **多源接收** - 支持 Prometheus、Zabbix 等多种监控系统的告警接入
- **系统标签** - 使用 `sys_` 前缀区分系统配置和业务标签，一目了然
- **可扩展性** - 轻松添加新的 Handler 和数据源，无需修改核心逻辑
- **零数据库依赖** - 完全基于内存运行，无需数据库支持

## 使用说明

### 环境要求

- Java 17 或更高版本
- Maven 3.6+

### 快速开始

#### 1. 构建项目

```bash
mvn clean package -DskipTests
```

#### 2. 配置告警规则

编辑 `src/main/resources/application.yml` 配置告警匹配规则：

```yaml
alert:
  rules:
    # 匹配标签：告警中带有这个标签的会匹配到这个规则
    - match_tag:
        channel: "kafka"
      # 系统标签：匹配后会把这些标签插入到告警中
      insert_tags:
        sys_enabled: "true"
        sys_dd_group: https://oapi.dingtalk.com/robot/send?access_token=YOUR_TOKEN
        sys_env: "production"
        sys_team: "ops"
```

#### 3. 启动应用

```bash
java -jar target/alert-link-1.0.0-SNAPSHOT.jar
```

或使用 Maven：

```bash
mvn spring-boot:run
```

### API 接口

#### 接收 Prometheus 告警

```bash
POST /prom-alert/abcd
Content-Type: application/json
```

接受标准 Prometheus Alertmanager webhook 格式的告警数据。

#### 健康检查

```bash
GET /api/receiver/health
```

响应示例：

```json
{
  "status": "UP",
  "cachedAlerts": 5,
  "receivers": ["prometheus", "zabbix", "custom"]
}
```

### 系统标签说明

系统标签以 `sys_` 为前缀，用于区分系统配置和业务标签：

| 标签 | 说明 | 示例 |
|-----|------|------|
| `sys_enabled` | 是否启用该规则 | `true` 或 `false` |
| `sys_dd_group` | 钉钉群组 webhook 地址 | `https://oapi.dingtalk.com/...` |
| `sys_env` | 环境标识 | `production`、`staging` |
| `sys_team` | 团队标识 | `ops`、`dev` |
| `sys_version` | 版本标识 | `1.0.0` |
| `sys_region` | 区域标识 | `cn-hangzhou` |

#### Prometheus 告警规则示例

```yaml
groups:
  - name: example
    rules:
      - alert: HighCPU
        expr: cpu_usage > 90
        for: 5m
        labels:
          severity: critical
          channel: kafka  # 匹配标签
        annotations:
          summary: "CPU 使用率过高 {{ $labels.instance }}"
```

## 设计理念

### 1. 管道式处理模式（Pipeline Pattern）

Alert Link 借鉴了 Netty Pipeline 的设计理念：

```
数据入口（头部）→ Handler1 → Handler2 → ... → 自然结束
```

- **有头无尾** - 数据从 `AlertMsgPipline.in()` 塞入，经过 Handler 链处理后自然结束
- **链式处理** - 每个 Handler 只关心自己的处理逻辑，处理完就传给下一个
- **侧效应驱动** - 通过修改 `AlertLinkMsg` 来传递状态，无需返回值

### 2. 标签驱动的路由机制

```
原始告警 → 匹配规则 → 插入系统标签 → 增强后的告警
```

- **匹配标签（match_tag）** - 定义匹配规则，告警中带有对应标签即匹配
- **插入标签（insert_tags）** - 匹配成功后追加的系统标签
- **一键识别** - 所有 `sys_` 前缀的标签都是系统级别的

### 3. 统一数据模型

所有数据源都转换为统一的 `AlertLinkMsg` 模型：

```java
public class AlertLinkMsg {
    private Object rawData;              // 原始数据
    private HttpServletRequest httpRequest;  // HTTP 请求
    private LinkedHashMap<String, String> attr;  // 控制参数（标签）
    private LinkedHashMap<String, String> showRootAttr;  // 展示参数
    private LinkedHashMap<String, LinkedHashMap<String, String>> showGroupAttr;  // 分组展示参数
}
```

### 4. 可扩展的 Handler 机制

```java
public interface AlertMsgHandler {
    boolean in(AlertLinkMsg alertLinkMsg);
}
```

每个 Handler 可以决定是否处理当前消息，通过标签判断是否适用。添加新的 Handler 只需：

1. 实现 `AlertMsgHandler` 接口
2. 添加 `@Component` 注解
3. 在 `AlertMsgPipline` 中注册

### 5. 配置驱动

所有配置通过 YAML 文件管理，支持：

- **多规则配置** - 配置多个匹配规则
- **灵活扩展** - 随时添加新的规则和标签
- **热更新** - 修改配置后重启即可生效

## 项目结构

```
src/main/java/io/github/xhrg/
├── AlertLinkApplication.java          # 主启动类
├── config/                            # 配置类
│   └── AlertRuleConfig.java          # 告警规则配置
├── constant/                          # 常量定义
│   └── SystemTags.java               # 系统标签常量
├── model/                             # 数据模型
│   ├── AlertLinkMsg.java             # 统一告警模型
│   └── PrometheusAlertMessage.java   # Prometheus 消息模型
├── pipline/                           # 管道处理
│   ├── AlertMsgPipline.java          # 管道入口
│   ├── AlertMsgHandler.java          # Handler 接口
│   └── impl/                         # Handler 实现
│       ├── TagsHandler.java          # 标签处理
│       ├── DingGroupHandler.java     # 钉钉路由
│       └── SmsHandler.java           # 短信处理
├── receiver/                          # 接收器
│   └── PrometheusAlertReceiver.java  # Prometheus 接收器
├── sender/                            # 发送器
│   └── DingGroupSender.java          # 钉钉发送器
└── util/                              # 工具类
    ├── JsonUtils.java                # JSON 工具
    ├── StrUtils.java                 # 字符串工具
    └── TimeUtils.java                # 时间工具
```

## 扩展开发

### 添加新的 Handler

```java
@Component
public class MyCustomHandler implements AlertMsgHandler {
    @Override
    public boolean in(AlertLinkMsg alertLinkMsg) {
        // 处理逻辑
        return true;
    }
}
```

### 添加新的数据源

```java
@Component
@RestController
@RequestMapping("/my-alert")
public class MyAlertReceiver {
    @Autowired
    private AlertMsgPipline alertMsgPipline;
    
    @PostMapping("/receive")
    public String receive(@RequestBody String body) {
        // 转换为 AlertLinkMsg
        AlertLinkMsg msg = new AlertLinkMsg();
        msg.setRawData(data);
        alertMsgPipline.in(msg);
        return "OK";
    }
}
```

## 许可证

Copyright 2026 xhrg

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
