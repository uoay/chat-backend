package net.uoay.chat.controller;

import jakarta.validation.Valid;
import net.uoay.chat.request.RegistrationRequest;
import net.uoay.chat.user.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
        @RequestBody @Valid RegistrationRequest request
    ) throws IllegalStateException {
        registrationService.registerUserAccount(request);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalStateException(IllegalStateException exception) {
        return exception.getMessage();
    }

}
