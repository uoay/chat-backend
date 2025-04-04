package net.uoay.chat.friend;

import jakarta.persistence.*;
import net.uoay.chat.user.Account;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_friendship")
public class Friendship implements Serializable {

    @EmbeddedId
    private FriendshipPrimeKey primeKey;

    @ManyToOne
    @MapsId("fromAccountId")
    private Account fromAccount;

    @ManyToOne
    @MapsId("toAccountId")
    private Account toAccount;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    Friendship() {}

    Friendship(Account fromAccount, Account toAccount) {
        primeKey = new FriendshipPrimeKey(fromAccount.getId(), toAccount.getId());
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    };

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public boolean contains(String username) {
        if (fromAccount.getUsername() == username || toAccount.getUsername() == username) {
            return true;
        }
        return false;
    }

    public Optional<String> getAnother(String username) {
        if (fromAccount.getUsername() == username) {
            return Optional.of(toAccount.getUsername());
        } else if (toAccount.getUsername() == username) {
           return Optional.of(fromAccount.getUsername());
        }
        return Optional.ofNullable(null);
    }

    public void remove() {
        fromAccount.removeFriend(this);
        toAccount.removeFriend(this);
    }

}
