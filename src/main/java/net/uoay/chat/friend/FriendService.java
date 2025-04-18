package net.uoay.chat.friend;

import net.uoay.chat.Utils;
import net.uoay.chat.redis.RedisService;
import net.uoay.chat.user.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private RedisService redisService;

    public Set<String> getFriends(String username) {
        return redisService
            .getSetIfExists(Utils.friendSetKey(username))
            .orElseGet(() -> {
                var account =  accountRepository.findByUsername(username).orElseThrow();
                var friends = account.getFriends();
                redisService.createStringSet(Utils.friendSetKey(username), friends);
                return friends;
            });
    }

    public boolean hasFriend(String username, String friendUsername) {
        return getFriends(username).contains(friendUsername);
    }

    @Transactional
    public void addFriend(String fromUser, String toUser) throws NoSuchElementException {
        if (!hasFriend(fromUser, toUser)) {
            var fromAccount = accountRepository.findByUsername(fromUser).orElseThrow();
            var toAccount = accountRepository.findByUsername(toUser).orElseThrow();

            var friendship = new Friendship(fromAccount, toAccount);

            fromAccount.addFriend(friendship);
            toAccount.addFriend(friendship);

            redisService.deleteFriendsCache(fromUser, toUser);
        }
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

        redisService.deleteFriendsCache(source, target);
    }
}
