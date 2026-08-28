## <th:block th:if="${status == 'FIRING'}">🔴 告警触发</th:block><th:block th:if="${status == 'RESOLVED'}">✅ 告警恢复</th:block><th:block th:if="${status != 'FIRING' and status != 'RESOLVED'}">告警通知</th:block>

<th:block th:if="${status == 'FIRING'}">
**告警名称**: <th:block th:text="${name} ?: '未知'"/>
**告警状态**: FIRING
**开始时间**: <th:block th:text="${startTime != null ? #dates.format(startTime, 'yyyy-MM-dd HH:mm:ss') : '未知'}"/>
</th:block>


<th:block th:if="${status == 'RESOLVED'}">
**告警名称**: <th:block th:text="${name} ?: '未知'"/>
**告警状态**: RESOLVED
**开始时间**: <th:block th:text="${startTime != null ? #dates.format(startTime, 'yyyy-MM-dd HH:mm:ss') : '未知'}"/>
**恢复时间**: <th:block th:text="${endTime != null ? #dates.format(endTime, 'yyyy-MM-dd HH:mm:ss') : '未知'}"/>
</th:block>

-----------告警详情---

<th:block th:each="entry : ${flatAttrs}">
<th:block th:if="${#strings.startsWith(entry.value, 'http')}">
[<th:block th:text="${entry.key}"/>](<th:block th:text="${entry.value}"/>)
</th:block>
<th:block th:unless="${#strings.startsWith(entry.value, 'http')}">
<th:block th:text="${entry.key}"/>:  <th:block th:text="${entry.value} ?: ''"/>
</th:block>
</th:block>
<th:block th:each="groupEntry : ${groupAttrs}">

**<th:block th:text="${groupEntry.key}"/>:**

<th:block th:each="item : ${groupEntry.value}">
*  <th:block th:text="${item.key}"/>:  <th:block th:text="${item.value} ?: ''"/>
</th:block>
</th:block>
