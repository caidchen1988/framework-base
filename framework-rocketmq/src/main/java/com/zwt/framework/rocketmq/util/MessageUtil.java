package com.zwt.framework.rocketmq.util;

import com.alibaba.rocketmq.common.message.Message;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author zwt
 * @detail
 * @date 2018/9/5
 * @since 1.0
 */
public class MessageUtil {
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    public static String getMessageJsonStr(Message message) {
        return String.format("{\"topic\":\"%s\",\"tags\":\"%s\",\"keys\":\"%s\",\"properties\":\"%s\",\"body\":\"%s\"}", message.getTopic(), message.getTags(), message.getKeys(), message.getProperties(), new String(message.getBody()));
    }
}
