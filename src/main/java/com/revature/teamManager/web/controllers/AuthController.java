package com.revature.teamManager.web.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.teamManager.services.CoachService;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.services.CoachService;
import com.revature.teamManager.services.RecruiterService;
import com.revature.teamManager.web.dtos.Credentials;
import com.revature.teamManager.web.dtos.Principal;
import com.revature.teamManager.web.util.security.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CoachService coachService;
    private final RecruiterService recruiterService;
    private final PlayerService playerService;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public AuthController(CoachService coachService,RecruiterService recruiterService, PlayerService playerService, ObjectMapper mapper, TokenGenerator tokenGenerator) {
        this.coachService = coachService;
        this.recruiterService = recruiterService;
        this.playerService = playerService;
        this.tokenGenerator = tokenGenerator;
    }

    @PostMapping(value="/coach",consumes = "application/json")
    public @ResponseBody Principal authenticateCoach(@RequestBody Credentials creds, HttpServletResponse resp) {
        Principal principal = coachService.login(creds.getUsername(), creds.getPassword());
        String token = tokenGenerator.createToken(principal);
        resp.setHeader(tokenGenerator.getJwtConfig().getHeader(), token);
        return principal;
    }

    @PostMapping(value="/recruiter",consumes = "application/json")
    public @ResponseBody Principal authenticateRecruiter(@RequestBody Credentials creds, HttpServletResponse resp) {
        Principal principal = recruiterService.login(creds.getUsername(), creds.getPassword());
        String token = tokenGenerator.createToken(principal);
        resp.setHeader(tokenGenerator.getJwtConfig().getHeader(), token);
        return principal;
    }

    @PostMapping(value="/player",consumes = "application/json")
    public @ResponseBody Principal authenticatePlayer(@RequestBody Credentials creds, HttpServletResponse resp) {
        Principal principal = playerService.login(creds.getUsername(), creds.getPassword());
        String token = tokenGenerator.createToken(principal);
        resp.setHeader(tokenGenerator.getJwtConfig().getHeader(), token);
        return principal;
    }

}
