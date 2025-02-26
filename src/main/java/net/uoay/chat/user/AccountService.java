package net.uoay.chat.user;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

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
        profileRepository.save(account.getProfile());
        accountRepository.save(account);
    }

    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Profile getProfile() {
        return accountRepository
            .findByUsername(getUsername())
            .get()
            .getProfile();
    }

    public void setProfile(Profile profile) {
        accountRepository
            .findByUsername(getUsername())
            .ifPresent(account -> {
                account.setProfile(profile);
                profileRepository.save(account.getProfile());
            });
    }

    public LocalDateTime getRegistrationTime() {
        return accountRepository
            .findByUsername(getUsername())
            .get()
            .getCreatedDate();
    }

}
