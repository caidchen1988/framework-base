package com.zwt.frameworkwebsocket.vo;

/**
 * @author zwt
 * @detail
 * @date 2019/1/10
 * @since 1.0
 */
public class ResponseMessage {

    private String content;

    public ResponseMessage() {
    }

    public ResponseMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
