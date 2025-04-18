package net.uoay.chat.controller;

import jakarta.validation.Valid;
import net.uoay.chat.group.ChatGroupService;
import net.uoay.chat.request.ChatGroupRequest;
import net.uoay.chat.request.CreateChatGroupRequest;
import net.uoay.chat.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
        chatGroupService.createGroup(
            accountService.getUsername(),
            request.searchId(),
            request.displayName()
        );
    }

    @GetMapping("/groups")
    public Set<String> getGroups() {
        return chatGroupService.getGroups(accountService.getUsername());
    }

    @PutMapping("/groups/join")
    public void joinGroup(@RequestBody @Valid ChatGroupRequest request) {
        chatGroupService.joinGroup(request.id(), accountService.getUsername());
    }

    @PutMapping("/groups/leave")
    public void leaveGroup(@RequestBody @Valid ChatGroupRequest request) {
        chatGroupService.leaveGroup(request.id(), accountService.getUsername());
    }

    @DeleteMapping("groups/delete")
    public void deleteGroup(@RequestBody @Valid ChatGroupRequest request) {
        chatGroupService.deleteGroup(request.id(), accountService.getUsername());
    }

    @GetMapping("group/{search_id}/members")
    public Set<String> getGroupMembers(
        @DestinationVariable("search_id") String searchId
    ) {
        return chatGroupService.getMembers(searchId);
    }

}
