package net.uoay.chat.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

    public Optional<Account> findByUsername(String username);

    public boolean existsByUsername(String username);

}
