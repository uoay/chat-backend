package net.uoay.chat.friend;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import net.uoay.chat.user.AccountRepository;
import net.uoay.chat.user.AccountService;

@Component
public class FriendService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private AccountService accountService;

    public List<String> getFriends() {
        return accountRepository
            .findByUsername(accountService.getUsername())
            .get()
            .getFriends();
    }

    @Transactional
    public void addFriend(String fromUser, String toUser) throws NoSuchElementException {
        var fromAccount = accountRepository.findByUsername(fromUser).orElseThrow();
        var toAccount = accountRepository.findByUsername(toUser).orElseThrow();

        var friendship = new Friendship(fromAccount, toAccount);

        fromAccount.addFriend(friendship);
        toAccount.addFriend(friendship);
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

    }

}
