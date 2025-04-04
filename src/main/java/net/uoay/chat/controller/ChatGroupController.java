package net.uoay.chat.controller;

import jakarta.validation.Valid;
import net.uoay.chat.group.ChatGroup;
import net.uoay.chat.group.ChatGroupService;
import net.uoay.chat.request.CreateChatGroupRequest;
import net.uoay.chat.request.JoinOrLeaveChatGroupRequest;
import net.uoay.chat.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class ChatGroupController {

    @Autowired
    ChatGroupService chatGroupService;

    @Autowired
    AccountService accountService;

    @PostMapping("/groups")
    @ResponseStatus(HttpStatus.CREATED)
    public void createGroup(@RequestBody @Valid CreateChatGroupRequest request) {
        chatGroupService.createGroup(accountService.getUsername(), request.displayName);
    }

    @GetMapping("/groups")
    public Set<ChatGroup> getGroups() {
        return chatGroupService.getGroups(accountService.getUsername());
    }

    @PutMapping("/groups/join")
    public void joinGroup(@RequestBody @Valid JoinOrLeaveChatGroupRequest request) {
        chatGroupService.joinGroup(request.id, accountService.getUsername());
    }

    @PutMapping("/groups/leave")
    public void leaveGroup(@RequestBody @Valid JoinOrLeaveChatGroupRequest request) {
        chatGroupService.leaveGroup(request.id, accountService.getUsername());
    }

}
