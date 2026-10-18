package com.safepay.safepay.Controller;




import com.safepay.safepay.Auth.AuthUser;
import com.safepay.safepay.Dto.RegisterRequest;

import com.safepay.safepay.Service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/me")
    public RegisterRequest.UserSummaryResponse me(@AuthenticationPrincipal AuthUser principal) {
        return userService.getById(principal.getId());
    }
}

