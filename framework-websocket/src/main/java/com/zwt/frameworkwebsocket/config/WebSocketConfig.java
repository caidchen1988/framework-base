package com.zwt.frameworkwebsocket.config;

import com.zwt.frameworkwebsocket.util.WebSocketConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author zwt
 * @detail  websocket配置
 * @date 2019/1/10
 * @since 1.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(WebSocketConstant.BARRAGE);
        config.setApplicationDestinationPrefixes(WebSocketConstant.PROJECT_PATH);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocketConstant.ENDPOINT_PATH).setAllowedOrigins("*").withSockJS();
    }
}
