package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.web.dtos.Offer;
import com.revature.teamManager.web.dtos.PlayerDTO;
import com.revature.teamManager.web.util.security.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Player registerNewPlayer(@RequestBody Player playerCandidate) {
        return playerService.register(playerCandidate);
    }

//    @Secured(allowedRoles = {"Coach"})
    @PutMapping(produces = "application/json", consumes = "application/json")
    public Player extendOffer(@RequestBody Offer newOffer){
        Player foundPlayer = playerService.updateOffers(newOffer);
        System.out.println(foundPlayer);

        return foundPlayer;
    }

    @Secured(allowedRoles = {"Player"})
    @GetMapping(value = "{username}", produces = "application/json")
    public PlayerDTO getInfo(@PathVariable String username) {
        Player player = playerService.getPlayerInfo(username);

        PlayerDTO response = new PlayerDTO(player);

        return response;
    }

}