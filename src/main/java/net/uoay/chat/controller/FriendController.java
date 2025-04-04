package net.uoay.chat.controller;

import net.uoay.chat.friend.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
