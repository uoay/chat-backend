package net.uoay.chat.controller;

import net.uoay.chat.friend.FriendService;
import net.uoay.chat.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Set;

@RestController
public class FriendController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private FriendService friendService;

    @GetMapping("/friends")
    public Set<String> getFriends() {
        return friendService.getFriends(accountService.getUsername());
    }

    @PutMapping("/friends")
    public void addFriend(@RequestBody String toUser) throws NoSuchElementException {
        var fromUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!fromUser.equals(toUser)) {
            friendService.addFriend(fromUser, toUser);
        }

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
