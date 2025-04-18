package net.uoay.chat.group;

import jakarta.transaction.Transactional;
import net.uoay.chat.Utils;
import net.uoay.chat.redis.RedisService;
import net.uoay.chat.user.Account;
import net.uoay.chat.user.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatGroupService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ChatGroupRepository chatGroupRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RedisService redisService;

    @Transactional
    public void createGroup(
        String ownerUsername,
        String searchId,
        String displayName
    ) throws IllegalArgumentException {
        if (chatGroupRepository.existsBySearchId(searchId)){
            throw new IllegalArgumentException(searchId + " already exists");
        }
        var owner = accountRepository.findByUsername(ownerUsername).orElseThrow();
        var group = new ChatGroup(owner, searchId, displayName);
        chatGroupRepository.save(group);
        owner.joinGroup(group);

        stringRedisTemplate.delete(Utils.chatGroupSetKey(ownerUsername));
    }

    public Set<String> getGroups(String username) {
        return redisService
            .getSetIfExists(Utils.chatGroupSetKey(username))
            .orElseGet(() -> {
                var account = accountRepository.findByUsername(username).orElseThrow();
                var groups = account.getGroups();
                redisService.createStringSet(Utils.chatGroupSetKey(username), groups);
                return groups;
            });
    }

    @Transactional
    public void joinGroup(String id, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findBySearchId(id).orElseThrow();
        group.addMember(account);
        account.joinGroup(group);

        stringRedisTemplate.delete(Utils.chatGroupSetKey(username));
    }

    @Transactional
    public void leaveGroup(String searchId, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findBySearchId(searchId).orElseThrow();
        if (!group.isOwner(account)) {
            group.removeMember(account);
            account.leaveGroup(group);

            stringRedisTemplate.delete(Utils.chatGroupSetKey(username));
        }
    }

    @Transactional
    public boolean isInGroup(String username, String searchId) {
        return getMembers(searchId).contains(username);
    }

    @Transactional
    public void deleteGroup(String searchId, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findBySearchId(searchId).orElseThrow();
        if (group.isOwner(account)) {
            var members = group.getMembers();
            stringRedisTemplate.delete(Utils.chatGroupMemberSetKey(group.getSearchId()));
            members.forEach(a -> a.leaveGroup(group));
            chatGroupRepository.delete(group);
            List<String> list = members
                .stream()
                .map(a -> Utils.chatGroupSetKey(a.getUsername()))
                .toList();
            redisService.deleteKeys(list);
        }
    }

    public Set<String> getMembers(String searchId) {
        return redisService
            .getSetIfExists(Utils.chatGroupMemberSetKey(searchId))
            .orElseGet(() -> {
                var group = chatGroupRepository.findBySearchId(searchId).orElseThrow();
                var members = group
                    .getMembers()
                    .stream()
                    .map(Account::getUsername)
                    .collect(Collectors.toSet());
                redisService.createStringSet(Utils.chatGroupMemberSetKey(searchId), members);
                return members;
            });
    }
}
