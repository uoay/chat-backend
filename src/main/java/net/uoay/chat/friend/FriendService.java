package net.uoay.chat.friend;

import net.uoay.chat.user.AccountRepository;
import net.uoay.chat.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class FriendService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private AccountService accountService;

    public Set<String> getFriends(String username) {
        var key = username + ":friends_set";
        if (!stringRedisTemplate.hasKey(key)) {
            var account =  accountRepository.findByUsername(username).orElseThrow();
            var friendsSet = account.getFriends();
            friendsSet.forEach(name ->
                stringRedisTemplate.opsForSet().add(key, name)
            );
            return friendsSet;
        };
        return stringRedisTemplate.opsForSet().members(key);
    }

    @Transactional
    public void addFriend(String fromUser, String toUser) throws NoSuchElementException {
        var fromAccount = accountRepository.findByUsername(fromUser).orElseThrow();
        var toAccount = accountRepository.findByUsername(toUser).orElseThrow();

        var friendship = new Friendship(fromAccount, toAccount);

        fromAccount.addFriend(friendship);
        toAccount.addFriend(friendship);

        stringRedisTemplate.delete(fromUser + ":friends_set");
        stringRedisTemplate.delete(toUser + ":friends_set");
    }

    @Transactional
    public void deleteFriend(String source, String target) throws NoSuchElementException {
        var sourceAccount = accountRepository.findByUsername(source).orElseThrow();
        var targetAccount = accountRepository.findByUsername(target).orElseThrow();

        friendshipRepository
            .findById(new FriendshipPrimeKey(sourceAccount.getId(), targetAccount.getId()))
            .ifPresent(friendship -> {
                friendship.remove();
                friendshipRepository.delete(friendship);
            });

        friendshipRepository
            .findById(new FriendshipPrimeKey(targetAccount.getId(), sourceAccount.getId()))
            .ifPresent(friendship -> {
                friendship.remove();
                friendshipRepository.delete(friendship);
            });

        stringRedisTemplate.opsForSet().remove(source + ":friends_set", target);
        stringRedisTemplate.opsForSet().remove(target + ":friends_set", source);

    }

    @Transactional
    public boolean isFriend(String username, String another) throws NoSuchElementException {
        var account = accountRepository.findByUsername(username).orElseThrow();
        if (account.hasFriend(another)) {
            return true;
        }
        return false;
    }

}
