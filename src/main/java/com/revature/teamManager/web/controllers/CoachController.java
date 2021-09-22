package com.revature.teamManager.web.controllers;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.services.CoachService;
import com.revature.teamManager.web.dtos.AssignPositionRequest;
import com.revature.teamManager.services.PlayerService;
import com.revature.teamManager.web.dtos.CoachDTO;
import com.revature.teamManager.web.dtos.Offer;
import com.revature.teamManager.web.util.security.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coach")
public class CoachController {
    private final CoachService coachService;
    private final PlayerService playerService;

    public CoachController(CoachService coachService, PlayerService playerService) {
        this.coachService = coachService;
        this.playerService = playerService;
    }

    //return a coach provided their username
    @GetMapping(value="{username}", produces = "application/json")
    public CoachDTO getCoach(@PathVariable String username) {
        Coach coach = coachService.getCoach(username);
        return new CoachDTO(coach);
    }

    //register a coach account
    @PostMapping(value="{pin}", produces = "application/json", consumes = "application/json")
    public Coach registerNewCoach(@RequestBody Coach coachCandidate, @PathVariable String pin) {
        return coachService.register(coachCandidate,pin);
    }

    //assigns a position to a player for their team
    @Secured(allowedRoles = {"Coach"})
    @PutMapping(value = "/positions", consumes = "application/json")
    public void assignPlayerPosition(@RequestBody AssignPositionRequest req) {
        coachService.assignPosition(req.getCoachUsername(), req.getPlayerUsername(), req.getPosition());
    }

    //adds a player to a team
    @Secured(allowedRoles = {"Player"})
    @PutMapping(value = "/team", produces = "application/json", consumes = "application/json")
    public Coach addPlayer(@RequestBody Offer accepted) {
        playerService.removeOffer(accepted);
        playerService.addTeam(getCoach(accepted.getCoachUsername()).getTeamName(), accepted.getPlayerUsername());
        return coachService.addPlayer(accepted.getCoachUsername(), accepted.getPlayerUsername());
    }

    //removes a player from a team
    @Secured(allowedRoles = {"Coach","Player"})
    @PatchMapping(value = "/team/remove", produces = "application/json", consumes = "application/json")
    public Coach removePlayer(@RequestBody Offer remove) {
        playerService.removeTeam(remove.getPlayerUsername());
        return coachService.removePlayer(remove.getCoachUsername(), remove.getPlayerUsername());
    }

    //assigns a workout to a player
    @Secured(allowedRoles = "Coach")
    @PatchMapping(value = "/assign/{username}", produces = "application/json", consumes = "application/json")
    public void assignWorkout(@RequestBody String exercise, @PathVariable String username){
        for (String[] teamPlayer : coachService.getTeamPlayers(username)) {
            if(!playerService.addExercise(teamPlayer[0],exercise))
                System.out.println("Exercise ["+exercise+"] is already assigned to team member ["+teamPlayer[0]+"]");

        }
    }

    //find the team that a player is on
    @GetMapping(value = "/player/{playerUsername}", produces = "application/json")
    public CoachDTO findPlayersTeam(@PathVariable String playerUsername) {
        Coach foundTeam = coachService.getTeamForPlayer(playerUsername);
        return new CoachDTO(foundTeam);
    }
}