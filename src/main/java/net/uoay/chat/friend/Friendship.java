package net.uoay.chat.friend;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.uoay.chat.user.Account;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@NoArgsConstructor
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
    @Column(nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private LocalDateTime createdDate;

    Friendship(Account fromAccount, Account toAccount) {
        primeKey = new FriendshipPrimeKey(fromAccount.getId(), toAccount.getId());
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    };

    public boolean contains(String username) {
        return fromAccount.getUsername().equals(username)
            || toAccount.getUsername().equals(username);
    }

    public Optional<String> getAnother(String username) {
        if (fromAccount.getUsername().equals(username)) {
            return Optional.of(toAccount.getUsername());
        } else if (toAccount.getUsername().equals(username)) {
           return Optional.of(fromAccount.getUsername());
        }
        return Optional.empty();
    }

    public void remove() {
        fromAccount.removeFriend(this);
        toAccount.removeFriend(this);
    }
}
