package net.uoay.chat.group;

import net.uoay.chat.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroup, Integer> {
    public Optional<ChatGroup> findBySearchId(String searchId);

    public boolean existsBySearchId(String searchId);
}
