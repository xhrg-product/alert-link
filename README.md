# Alert Link

> 轻量级告警路由中间件，解决监控告警源与通知渠道的解耦问题。

## 产品介绍

**痛点**：监控系统（Prometheus/Zabbix 等）直接对接通知渠道（钉钉/短信等），导致：
- 每个监控源都要单独配置通知渠道，重复劳动
- 告警路由规则散落在各监控系统，难以统一管理
- 新增通知渠道需要修改所有监控源配置

**Alert Link 作为告警中间层**，统一接收所有监控源的告警，通过标签匹配规则自动路由到对应的通知渠道。监控源只需配置 webhook 指向 Alert Link，无需关心后续分发逻辑。

## 使用说明

### 快速启动

```bash
java -jar alert-link.jar
```

### 配置告警路由规则

编辑 `application.yml`：

```yaml
alert:
  rules:
    - match_tag:
        channel: "kafka"        # 匹配条件：告警标签包含 channel=kafka
      insert_tags:
        sys_dd_group: https://oapi.dingtalk.com/robot/send?access_token=xxx  # 路由到钉钉群
        sys_env: "production"
```

### 接入监控源

Alert Link 目前支持两种告警接收方式：

**1. Alertmanager Webhook**
- 端点：`POST /for-alertmanager`
- 说明：接收 Alertmanager 发送的 webhook 告警
- 配置：在 Alertmanager 的 `receivers` 中配置 webhook URL

**2. Prometheus 原生格式**
- 端点：`POST /api/v2/alerts`
- 说明：接收 Prometheus 原生的告警数据格式
- 特性：支持告警去重和静默机制（20分钟内恢复通知只发送一次）

**接入示例**：
```yaml
# Alertmanager 配置示例
receivers:
  - name: 'alert-link'
    webhook_configs:
      - url: 'http://alert-link-server:8787/for-alertmanager'
        send_resolved: true
```

**其他监控系统**：扩展 Receiver 即可支持新的告警源

### 通知模板

钉钉通知模板位于 `src/main/resources/templates/dd-group.md`，使用 Thymeleaf 语法，可根据告警状态（FIRING/RESOLVED）动态渲染。

## 设计思路

### 核心：多入口路由设计（一盏灯，多个开关）

通知渠道地址（如钉钉群 webhook）的配置位置是灵活的，可以配置在任意节点：

- **配置在告警源**：Prometheus 告警规则中直接带上 `sys_dd_group` 标签
- **配置在 Alert Link**：通过 `match_tag` 匹配后注入 `insert_tags`
- **配置在其他中间件**：任何上游系统都可以预先注入路由标签

这就像"一盏灯有多个开关"——同一个钉钉群，可以从多个告警源触发，每个告警源都可以独立配置是否发送到该群。Alert Link 的核心价值是**统一路由逻辑**，而非集中管理所有配置。

```
─────────────┐     ┌──────────────┐     ┌─────────────┐
│  Prometheus │────▶│              │────▶│  钉钉群 A    │  ← 配置在Prometheus规则中
─────────────┘     │              │     └─────────────┘
                    │  Alert Link  │
─────────────     │              │     ┌─────────────┐
│   Zabbix    │────▶│              │────▶│  钉钉群 A    │  ← 配置在Alert Link YAML中
└─────────────┘     └──────────────┘     └─────────────
```

**解耦关键**：
1. **统一入口**：所有监控源转换为 `AlertLinkMsg` 统一模型
2. **标签驱动路由**：Handler 读取 `sys_dd_group` 等系统标签决定发送目标，标签来源不限
3. **管道式处理**：Handler 链依次处理，每个 Handler 只关心自己负责的逻辑

### 处理流程

```
告警进入 → TagsHandler(匹配规则，注入系统标签) → SkipHandler(跳过已处理) → DingGroupHandler(发送钉钉) → SmsHandler(发送短信)
```

- **TagsHandler**：根据配置的 `match_tag` 匹配告警，注入 `sys_` 前缀的系统标签
- **DingGroupHandler**：读取 `sys_dd_group` 标签，发送到对应钉钉群
- **SmsHandler**：读取短信相关标签，发送短信通知

### 扩展方式

- **新增监控源**：添加 Receiver，将数据转换为 `AlertLinkMsg`
- **新增通知渠道**：实现 `AlertMsgHandler` 接口，读取对应的系统标签
- **新增路由规则**：修改 YAML 配置，无需改代码
