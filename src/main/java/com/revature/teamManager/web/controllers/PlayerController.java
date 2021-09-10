package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.services.PlayerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Player registerNewCoach(@RequestBody Player playerCandidate) {
        return playerService.register(playerCandidate);
    }
}