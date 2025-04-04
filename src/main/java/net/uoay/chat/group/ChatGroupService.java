package net.uoay.chat.group;

import jakarta.transaction.Transactional;
import net.uoay.chat.user.Account;
import net.uoay.chat.user.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ChatGroupService {
    private Logger logger = LoggerFactory.getLogger(ChatGroupService.class);

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void createGroup(String owner_username, String displayName) {
        var owner = accountRepository.findByUsername(owner_username).orElseThrow();
        var group = new ChatGroup(owner, displayName);
        chatGroupRepository.save(group);
        owner.joinGroup(group);
        accountRepository.save(owner);
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
        chatGroupRepository.save(group);
        accountRepository.save(account);
    }

    @Transactional
    public void leaveGroup(Integer id, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findById(id).orElseThrow();
        group.removeMember(account);
        account.leaveGroup(group);
        chatGroupRepository.save(group);
        accountRepository.save(account);
    }

    @Transactional
    public boolean isInGroup(String username, Integer groupId) {
        Account account = accountRepository.findByUsername(username).orElseThrow();
        ChatGroup group = chatGroupRepository.findById(groupId).orElseThrow();
        return group.contains(account);
    }
}
