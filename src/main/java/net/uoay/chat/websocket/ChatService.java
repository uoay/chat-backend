package net.uoay.chat.websocket;

import net.uoay.chat.friend.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private FriendService friendService;

    public void sendToFriend(String from, String to, String msg) {
        if (friendService.hasFriend(from, to)) {
            messagingTemplate.convertAndSendToUser(to, "private", msg);
        }
    }

    public void sendToGroup(String to, String msg) {
        messagingTemplate.convertAndSend("/group/" + to, msg);
    }
}
