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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    public static String timeToLocal(String time) {

        if (time == null || time.isEmpty()) {
            return "";
        }

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date d = df.parse(time);
            return new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(d);
        } catch (Exception e) {

        }
        return "";
    }

    public static String timeToLocal(Date time) {

        if (time == null) {
            return "";
        }

        try {
            return new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(time);
        } catch (Exception e) {

        }
        return "";
    }
}
