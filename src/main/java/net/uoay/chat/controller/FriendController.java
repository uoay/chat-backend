package net.uoay.chat.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @PostMapping("/friends")
    public void addFriend(@RequestBody String toUser) throws NoSuchElementException {
        var fromUser = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.addFriend(fromUser, toUser);
    }

    @DeleteMapping("/friends")
    public void deleteFriend(@RequestBody String target) throws NoSuchElementException {
        var source = SecurityContextHolder.getContext().getAuthentication().getName();
        friendService.deleteFriend(source, target);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNoSuchElementException(NoSuchElementException exception) {
        return "Invalid username";
    }

}
