package net.uoay.chat.friend;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FriendshipPrimeKey implements Serializable {

    @Column(nullable = false)
    private Integer fromAccountId;

    @Column(nullable = false)
    private Integer toAccountId;

    public FriendshipPrimeKey() {}

    public FriendshipPrimeKey(Integer fromAccountId, Integer toAccountId) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FriendshipPrimeKey) {
            var key = (FriendshipPrimeKey) obj;
            return this == obj
                || Objects.equals(key.fromAccountId, fromAccountId)
                && Objects.equals(key.toAccountId, toAccountId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromAccountId, toAccountId);
    }

    public void setFromAccountId(Integer fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public void setToAccountId(Integer toAccountId) {
        this.toAccountId = toAccountId;
    }

}
