package net.uoay.chat.group;

import jakarta.transaction.Transactional;
import net.uoay.chat.Utils;
import net.uoay.chat.redis.RedisService;
import net.uoay.chat.user.Account;
import net.uoay.chat.user.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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

        redisService.addToSetIfExists(Utils.chatGroupSetKey(ownerUsername), searchId);
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
    public void joinGroup(String searchId, String username) throws NoSuchElementException {
        var group = chatGroupRepository.findBySearchId(searchId).orElseThrow();
        var account = accountRepository.findByUsername(username).orElseThrow();

        group.addMember(account);
        account.joinGroup(group);

        redisService.addToSetIfExists(Utils.chatGroupSetKey(username), searchId);
        redisService.addToSetIfExists(Utils.chatGroupMemberSetKey(searchId), username);
    }

    @Transactional
    public void leaveGroup(String searchId, String username) {
        var account = accountRepository.findByUsername(username).orElseThrow();
        var group = chatGroupRepository.findBySearchId(searchId).orElseThrow();
        if (!group.isOwner(account)) {
            group.removeMember(account);
            account.leaveGroup(group);

            stringRedisTemplate.opsForSet().remove(Utils.chatGroupSetKey(username), searchId);
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
            for (var member : members) {
                member.leaveGroup(group);
            }
            chatGroupRepository.delete(group);
            stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
                @Override
                public <K, V> List<Object> execute(
                    RedisOperations<K, V> operations
                ) throws DataAccessException {
                    operations.multi();
                    var ops = operations.opsForSet();
                    for (var member: members) {
                        ops.remove(
                            (K) Utils.chatGroupSetKey(member.getUsername()),
                            searchId
                        );
                    }
                    return operations.exec();
                }
            });
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
