package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.web.dtos.Offer;
import com.revature.teamManager.web.dtos.PlayerDTO;
import com.revature.teamManager.web.util.security.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PutMapping(value = "/{operation}")
    public Player extendOffer(@RequestBody Offer newOffer, @PathVariable("operation") String operation){
        Player foundPlayer = playerService.updateOffers(newOffer, operation);
        return foundPlayer;
    }

    @Secured(allowedRoles = {"Player"})
    @GetMapping(value = "/user/{username}", produces = "application/json")
    public PlayerDTO getInfo(@PathVariable String username) {
        Player player = playerService.getPlayerInfo(username);

        PlayerDTO response = new PlayerDTO(player);

        return response;
    }

	@GetMapping(value = "/{sport}", produces = "application/json")
	public List<Player> getAllUsers(@PathVariable("sport") String sport) {
		return playerService.findPlayersBySport(sport);
	}
}