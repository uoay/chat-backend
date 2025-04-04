package net.uoay.chat.controller;

import jakarta.validation.Valid;
import net.uoay.chat.Utils;
import net.uoay.chat.user.AccountService;
import net.uoay.chat.user.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/profile")
    public Profile getProfile(
        @RequestBody String username
    ) throws NoSuchElementException, IllegalArgumentException {
        if (username.matches(Utils.usernamePattern)) {
            return accountService.getProfile(username).orElseThrow();
        }
        throw new IllegalArgumentException();
    }

    @PostMapping("/profile")
    public void setProfile(@RequestBody @Valid Profile profile) {
        accountService.setProfile(profile);
    }

    @GetMapping("/username")
    public String getUsername() {
        return accountService.getUsername();
    }

    @GetMapping("/registration_time")
    public LocalDateTime getRegistrationTime() {
        return accountService.getRegistrationTime();
    }

}
