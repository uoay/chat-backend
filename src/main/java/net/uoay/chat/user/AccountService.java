package net.uoay.chat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public void register(Account account) throws IllegalStateException {
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new IllegalStateException(account.getUsername() + " already exists");
        }
        accountRepository.save(account);
    }

}
