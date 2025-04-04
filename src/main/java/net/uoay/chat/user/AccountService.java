package net.uoay.chat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private RedisTemplate<String, Profile> profileRedisTemplate;

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

    public Optional<Profile> getProfile(String username) {
        var key = username + ":profile";
        if (!profileRedisTemplate.hasKey(key)) {
            accountRepository
                .findByUsername(username)
                .ifPresent(account ->
                    profileRedisTemplate.opsForValue().set(key, account.getProfile())
                );
        }
        return Optional.ofNullable(profileRedisTemplate.opsForValue().get(key));
    }

    public void setProfile(Profile profile) {
        var username = getUsername();
        accountRepository
            .findByUsername(getUsername())
            .ifPresent(account -> {
                account.setProfile(profile);
                profileRedisTemplate.opsForValue().set(username + ":profile", profile);
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
