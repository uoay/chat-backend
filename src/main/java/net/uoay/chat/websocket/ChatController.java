package net.uoay.chat.websocket;

import net.uoay.chat.group.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatGroupService chatGroupService;

    @MessageMapping("/user/{username}")
    public void sendToFriend(
        @DestinationVariable("username") String username,
        @Payload String msg,
        Principal principal
    ) {
        chatService.sendToFriend(principal.getName(), username, msg);
    }

    @MessageMapping("/group/{group_id}")
    public void sendToGroup(
        @DestinationVariable("group_id") Integer id,
        @Payload String msg,
        Principal principal
    ) {
        var username = principal.getName();
        if (chatGroupService.isInGroup(username, id)) {
            chatService.sendToGroup(id, msg);
        }
    }

}
