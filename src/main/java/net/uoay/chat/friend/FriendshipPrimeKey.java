package net.uoay.chat.friend;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

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
        if (obj instanceof FriendshipPrimeKey key) {
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
