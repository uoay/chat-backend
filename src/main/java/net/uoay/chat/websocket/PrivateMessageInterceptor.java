package net.uoay.chat.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class PrivateMessageInterceptor implements ChannelInterceptor {

    @Override
    @Nullable
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            var destination = accessor.getDestination();
            if (destination !=null && destination.endsWith("private")) {
                var principal = accessor.getUser();
                if (principal != null
                    && destination.equals("/user/" + principal.getName() + "/private")
                ) {
                    return message;
                }
                return null;
            }
        }
        return message;
    }

}
