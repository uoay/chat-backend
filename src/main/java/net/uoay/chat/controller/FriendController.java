package net.uoay.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.uoay.chat.friend.FriendService;

@RestController
public class FriendController {

    @Autowired
    FriendService friendService;

    @GetMapping("/friends")
    public List<String> getFriends() {
        return friendService.getFriends();
    }

    @PostMapping("/add_friend")
    public void addFriend(@RequestBody String toUser) {
        var fromUser = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.addFriend(fromUser, toUser);
    }

}
