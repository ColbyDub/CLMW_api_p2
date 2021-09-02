package com.revature.teamManager.web.controllers;


import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.web.dtos.Credentials;
import com.revature.teamManager.web.dtos.Principal;
import com.revature.teamManager.web.util.security.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PlayerService playerService;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public AuthController(PlayerService playerService, TokenGenerator tokenGenerator) {
        this.playerService = playerService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Principal authenticate(@RequestBody Credentials credentials, HttpServletResponse resp) {
        //Principal principal = userService.login(credentials.getUsername(), credentials.getPassword());
        //resp.setHeader(tokenGenerator.getJwtHeader(), tokenGenerator.createToken(principal));
        return new Principal();
    }

}
