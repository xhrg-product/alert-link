# Prometheus 告警时间规律

## 告警时间字段规律

1. `start` 是第一次触发告警的时间，后续告警推送中保持不变
2. `end` 是当前推送时间 + 4分钟，每次推送都会向前滚动
3. Prometheus 告警推送频率为 1 分钟 1 次，与 `evaluation_interval` 和 rule 的 `for` 参数无关
