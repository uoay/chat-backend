package net.uoay.chat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.uoay.chat.request.RegistrationRequest;

@Service
public class RegistrationService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUserAccount(RegistrationRequest request) throws IllegalStateException {
        Account account = new Account(
            request.username,
            passwordEncoder.encode(request.password),
            new Profile(request.display_name)
        );
        accountService.register(account);
    }
}
