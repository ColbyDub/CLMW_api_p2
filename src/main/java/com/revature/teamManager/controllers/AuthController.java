package com.revature.teamManager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.web.dtos.Credentials;
import com.revature.teamManager.web.dtos.Principal;
import com.revature.teamManager.web.util.security.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PlayerService playerService;
    private final ObjectMapper mapper;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public AuthController(PlayerService playerService, ObjectMapper mapper, TokenGenerator tokenGenerator) {
        this.playerService = playerService;
        this.mapper = mapper;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping(value="/login",consumes = "application/json")
    public @ResponseBody
    String authenticateUser(@RequestBody Credentials creds, HttpServletResponse resp) {
        Principal principal = userService.login(creds.getUsername(), creds.getPassword(), creds.getRole());
        String token = tokenGenerator.createToken(principal);
        resp.setHeader(tokenGenerator.getJwtConfig().getHeader(), token);
        return principal.toString();
        return "replace this";
    }
}
