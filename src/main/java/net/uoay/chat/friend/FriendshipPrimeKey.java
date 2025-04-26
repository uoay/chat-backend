package net.uoay.chat.friend;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FriendshipPrimeKey implements Serializable {
    @Column(nullable = false)
    private Integer fromAccountId;

    @Column(nullable = false)
    private Integer toAccountId;

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
}
