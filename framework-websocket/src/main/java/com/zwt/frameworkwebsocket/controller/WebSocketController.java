package com.zwt.frameworkwebsocket.controller;

import com.zwt.frameworkwebsocket.util.WebSocketConstant;
import com.zwt.frameworkwebsocket.vo.ResponseMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author zwt
 * @detail
 * @date 2019/1/10
 * @since 1.0
 */
@Controller
public class WebSocketController {

    //@MessageMapping和@RequestMapping功能类似，用于设置URL映射地址，浏览器向服务器发起请求，需要通过该地址。
    @MessageMapping(WebSocketConstant.REQUEST_PATH)
    //如果服务器接受到了消息，就会对订阅了@SendTo括号中的地址传送消息。
    @SendTo(WebSocketConstant.BARRAGE)
    public ResponseMessage barrage(ResponseMessage message){
        return message;
    }
}
