package com.foticc.upms.controller;

import com.foticc.security.annotation.InnerAPI;
import com.foticc.upms.client.dto.SysAuthUserDTO;
import com.foticc.upms.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class UserController {


    private final LoginService loginService;

    public UserController(LoginService loginService) {
        this.loginService = loginService;
    }

    @InnerAPI
    @PostMapping("/load")
    public SysAuthUserDTO loginInfo(String username) {
        return loginService.loadUserByUsername(username);
    }

}
