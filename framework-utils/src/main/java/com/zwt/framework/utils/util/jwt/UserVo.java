package com.zwt.framework.utils.util.jwt;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zwt
 * @detail
 * @date 2019/1/21
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class UserVo {
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户唯一Id
     */
    private String userId;
    /**
     * sessionId
     */
    private String sessionId;
    /**
     * 过期时间
     */
    private long expiresAt;
    /**
     * 用户所属平台
     */
    private String platform;
}
