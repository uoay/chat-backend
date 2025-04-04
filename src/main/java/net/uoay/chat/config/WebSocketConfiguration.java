package net.uoay.chat.config;

import net.uoay.chat.group.ChatGroupService;
import net.uoay.chat.websocket.GroupChatMessageInterceptor;
import net.uoay.chat.websocket.PrivateMessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
@EnableScheduling
public class WebSocketConfiguration extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {

    @Autowired
    private ChatGroupService chatGroupService;

    @Override
    public void configureStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/")
            .setAllowedOrigins("*");
	}

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/user", "/group");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        super.configureClientInboundChannel(registration);
        registration.interceptors(
            new PrivateMessageInterceptor(),
            new GroupChatMessageInterceptor(chatGroupService)
        );
    }

}
