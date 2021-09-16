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

    @GetMapping(value="{username}", produces = "application/json")
    public CoachDTO getCoach(@PathVariable String username) {
        Coach coach = coachService.getCoach(username);
        return new CoachDTO(coach);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public Coach registerNewCoach(@RequestBody Coach coachCandidate) {
        return coachService.register(coachCandidate);
    }

    @PutMapping(value = "/positions", consumes = "application/json")
    public void assignPlayerPosition(@RequestBody AssignPositionRequest req) {
        coachService.assignPosition(req.getCoachUsername(), req.getPlayerUsername(), req.getPosition());
    }

    @PutMapping(value = "/team", produces = "application/json", consumes = "application/json")
    public Coach addPlayer(@RequestBody Offer accepted) {
        playerService.removeOffer(accepted);
        playerService.addTeam(getCoach(accepted.getCoachUsername()).getTeamName(), accepted.getPlayerUsername());
        return coachService.addPlayer(accepted.getCoachUsername(), accepted.getPlayerUsername());
    }

    @PatchMapping(value = "/team/remove", produces = "application/json", consumes = "application/json")
    public Coach removePlayer(@RequestBody Offer remove) {
        playerService.removeTeam(remove.getPlayerUsername());
        return coachService.removePlayer(remove.getCoachUsername(), remove.getPlayerUsername());
    }

    @Secured(allowedRoles = "Coach")
    @PatchMapping(value = "/assign/{username}", produces = "application/json", consumes = "application/json")
    public void assignWorkout(@RequestBody String exercise, @PathVariable String username){
        for (String teamPlayer[] : coachService.getTeamPlayers(username)) {
            if(!playerService.addExercise(teamPlayer[0],exercise))
                System.out.println("Exercise ["+exercise+"] is already assigned to team member ["+teamPlayer[0]+"]");

        }
    }

    @GetMapping(value = "/player/{playerUsername}", produces = "application/json")
    public CoachDTO findPlayersTeam(@PathVariable String playerUsername) {
        Coach foundTeam = coachService.getTeamForPlayer(playerUsername);
        CoachDTO teamDTO = new CoachDTO(foundTeam);
        return teamDTO;
    }
}