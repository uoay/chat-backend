package net.uoay.chat.group;

import jakarta.transaction.Transactional;
import net.uoay.chat.user.Account;
import net.uoay.chat.user.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void createGroup(String ownerUsername, String displayName) {
        var owner = accountRepository.findByUsername(ownerUsername).orElseThrow();
        var group = new ChatGroup(owner, displayName);
        chatGroupRepository.save(group);
        owner.joinGroup(group);
        accountRepository.save(owner);
        stringRedisTemplate.delete(ownerUsername + ":chatGroupSet");
    }

    public Set<String> getGroups(String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var key = username + ":chatGroupSet";
        if (!stringRedisTemplate.hasKey(key)) {
            var groups = account.getGroups();
            groups.forEach(id ->
                stringRedisTemplate.opsForSet().add(key, id)
            );
            return groups;
        }
        return stringRedisTemplate.opsForSet().members(key);
    }

    @Transactional
    public void joinGroup(Integer id, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findById(id).orElseThrow();
        group.addMember(account);
        account.joinGroup(group);
        chatGroupRepository.save(group);
        accountRepository.save(account);
        stringRedisTemplate.delete(username + ":chatGroupSet");
    }

    @Transactional
    public void leaveGroup(Integer id, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findById(id).orElseThrow();
        if (!group.isOwner(account)) {
            group.removeMember(account);
            account.leaveGroup(group);
            chatGroupRepository.save(group);
            accountRepository.save(account);
            stringRedisTemplate.opsForSet().remove(
                username + ":chatGroupSet",
                String.valueOf(group.getId())
            );
        }
    }

    @Transactional
    public boolean isInGroup(String username, Integer groupId) {
        return getMembers(groupId).contains(username);
    }

    @Transactional
    public void deleteGroup(Integer groupId, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findById(groupId).orElseThrow();
        if (group.isOwner(account)) {
            var members = group.getMembers();
            var id = group.getId();
            members.forEach(a -> a.leaveGroup(group));
            chatGroupRepository.delete(group);
            members.forEach(a ->
                stringRedisTemplate.opsForSet().remove(
                    a.getUsername() + ":chatGroupSet",
                    String.valueOf(id)
                )
            );
        }
    }

    public Set<String> getMembers(Integer id) {
        var group = chatGroupRepository.findById(id).orElseThrow();
        var key = "chatGroup:" + id;
        if (!stringRedisTemplate.hasKey(key)) {
            var members = group
                .getMembers()
                .stream()
                .map(Account::getUsername)
                .collect(Collectors.toSet());
            members.forEach(username -> stringRedisTemplate.opsForSet().add(username));
            return members;
        }
        return stringRedisTemplate.opsForSet().members(key);
    }
}
