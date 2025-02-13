package net.uoay.chat.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.uoay.chat.request.RegistrationRequest;

@RestController
@RequestMapping("/registration")
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
    @ResponseStatus
    public String handleIllegalStateException(IllegalStateException exception) {
        return exception.getMessage();
    }

}
