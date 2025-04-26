package net.uoay.chat.websocket;

import net.uoay.chat.group.ChatGroupService;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class GroupChatMessageInterceptor implements ChannelInterceptor {
    private final ChatGroupService chatGroupService;

    public GroupChatMessageInterceptor(ChatGroupService chatGroupService) {
        this.chatGroupService = chatGroupService;
    }

    @Override
    @Nullable
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination !=null && destination.startsWith("/group")) {
                String[] parts = destination.split("/");
                var principal = accessor.getUser();
                if (principal != null
                    && chatGroupService.isInGroup(principal.getName(), parts[parts.length - 1])
                ) {
                    return message;
                }
                return null;
            }
        }
        return message;
    }
}
