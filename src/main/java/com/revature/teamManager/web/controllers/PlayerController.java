package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.web.dtos.Offer;
import com.revature.teamManager.web.util.security.Secured;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
)

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

//    @Secured(allowedRoles = {"coach"})
    @PutMapping(produces = "application/json", consumes = "application/json")
    public Player extendOffer(@RequestBody Offer newOffer){
        Player foundPlayer = playerService.updateOffers(newOffer);
        System.out.println(foundPlayer);

        return foundPlayer;
    }

}
	
	@GetMapping(produces = "application/json")
	public List<Player> getAllUsers() {
		return playerService.findAll();
	}
}
