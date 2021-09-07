package com.revature.teamManager.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.teamManager.services.CoachService;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.services.UserService;
import com.revature.teamManager.web.dtos.Credentials;
import com.revature.teamManager.web.dtos.Principal;
import com.revature.teamManager.web.util.security.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final CoachService coachService;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public AuthController(UserService userService, CoachService coachService, TokenGenerator tokenGenerator) {
        this.userService = userService;
        this.coachService = coachService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping(value="/coach",consumes = "application/json")
    public @ResponseBody String authenticateCoach(@RequestBody Credentials creds, HttpServletResponse resp) {
        Principal principal = coachService.login(creds.getUsername(), creds.getPassword());
        String token = tokenGenerator.createToken(principal);
        resp.setHeader(tokenGenerator.getJwtConfig().getHeader(), token);
        return principal.toString();
    }

}
