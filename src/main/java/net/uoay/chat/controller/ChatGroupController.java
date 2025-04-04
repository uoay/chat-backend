package net.uoay.chat.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.uoay.chat.group.ChatGroup;
import net.uoay.chat.group.ChatGroupService;
import net.uoay.chat.request.CreateChatGroupRequest;
import net.uoay.chat.request.JoinOrLeaveChatGroupRequest;
import net.uoay.chat.user.AccountService;

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
