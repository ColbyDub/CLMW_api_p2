package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.web.dtos.*;
import com.revature.teamManager.web.util.security.Secured;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
        return playerService.updateOffers(newOffer, operation);
    }

    @PutMapping(value="/skill", produces = "application/json", consumes = "application/json")
    public Player addSkill(@RequestBody AddToProfile addToProfile) {
        return playerService.addSkill(addToProfile.getUsername(), addToProfile.getAddedValue());
    }

    @PutMapping(value="/skill/rate")
    public void rateSkill(@RequestParam String username, @RequestParam String skill, @RequestParam int rating) {
        playerService.rateSkill(username, skill, rating);
    }

    @PutMapping(value="/sport", produces = "application/json", consumes = "application/json")
    public Player addSport(@RequestBody AddToProfile addToProfile) {
        return playerService.addSport(addToProfile.getUsername(), addToProfile.getAddedValue());
    }

    @GetMapping(value = "/{sport}", produces = "application/json")
    public List<Player> getAllUsersBySport(@PathVariable("sport") String sport) {
        return playerService.findPlayersBySport(sport);
    }

    @PutMapping(value="/skill/manage", produces = "application/json", consumes = "application/json")
    public Player deleteSkill(@RequestBody AddToProfile addToProfile) {
        return playerService.deleteSkill(addToProfile.getUsername(), addToProfile.getAddedValue());
    }

    @PutMapping(value="/sport/manage", produces = "application/json", consumes = "application/json")
    public Player deleteSport(@RequestBody AddToProfile addToProfile) {
        return playerService.deleteSport(addToProfile.getUsername(), addToProfile.getAddedValue());
    }

	@GetMapping(produces = "application/json")
	public List<Player> getAllUsers() {
		return playerService.findAll();
	}


    @Secured(allowedRoles = {"Player"})
    @GetMapping(value = "/user/{username}", produces = "application/json")
    public PlayerDTO getInfo(@PathVariable String username) {
        Player player = playerService.getPlayerInfo(username);

        return new PlayerDTO(player);
    }

    @Secured(allowedRoles = {"Player"})
    @GetMapping(value = "/exercises/{username}", produces = "application/json")
    public List<String> getExercises(@PathVariable String username) {
        PlayerDTO player = getInfo(username);

        return player.getExercises();
    }

    @PutMapping(value = "/exercise/{operation}")
    public Player modifyExercise(@RequestBody ModifyExercise changedExercise, @PathVariable("operation") String operation){
        Player foundPlayer = playerService.modifyExercise(changedExercise, operation);
        return foundPlayer;
    }
}

