package net.uoay.chat.group;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import net.uoay.chat.user.AccountRepository;

@Service
public class ChatGroupService {

    @Autowired
    ChatGroupRepository chatGroupRepository;

    @Autowired
    AccountRepository accountRepository;

    @Transactional
    public void createGroup(String owner_username, String displayName) {
        var owner = accountRepository.findByUsername(owner_username).orElseThrow();
        var group = new ChatGroup(owner, displayName);
        group.addMember(owner);
        group.setOwner(owner);
        owner.joinGroup(group);
    }

    public Set<ChatGroup> getGroups(String username) {
        return accountRepository
            .findByUsername(username)
            .orElseThrow()
            .getGroups();
    }

    @Transactional
    public void joinGroup(Integer id, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findById(id).orElseThrow();
        group.addMember(account);
        account.joinGroup(group);
    }

    @Transactional
    public void leaveGroup(Integer id, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findById(id).orElseThrow();
        group.removeMember(account);
        account.leaveGroup(group);
    }
}
