package net.uoay.chat.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import net.uoay.chat.friend.FriendService;

@Service
public class ChatService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private FriendService friendService;

    public void sendToFriend(String from, String to, String msg) {
        if (friendService.isFriend(from, to)) {
            messagingTemplate.convertAndSendToUser(to, "private", msg);
        }
    }

}
