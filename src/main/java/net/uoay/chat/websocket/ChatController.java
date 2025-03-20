package net.uoay.chat.websocket;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private ChatService chatService;

    @MessageMapping("/user/{username}")
    public void sendToFriend(
        @DestinationVariable("username") String username,
        @Payload String msg,
        Principal principal
    ) {
        chatService.sendToFriend(principal.getName(), username, msg);
    }

}
