package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.repos.PlayerRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.util.exceptions.ResourcePersistenceException;
import com.revature.teamManager.web.dtos.Offer;
import com.revature.teamManager.web.dtos.Principal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private PlayerRepository playerRepository;
    private PasswordUtils passwordUtils;

    public PlayerService(PlayerRepository playerRepository, PasswordUtils passwordUtils){
        this.playerRepository = playerRepository;
        this.passwordUtils = passwordUtils;
    }

    public boolean isValid(Player player){

        if(player.getName() == "" || player.getUsername() == "" || player.getPassword() == "" || player.getSport() == "" || player.getPassword().length() <= 7){
            throw new InvalidRequestException("invalid user data");
        }

        if(playerRepository.findPlayerByUsername(player.getUsername()) != null){
            throw new ResourcePersistenceException("username is taken");
        }

        return true;
    }

    public Player register(Player player){

        player.setPassword(passwordUtils.generateSecurePassword(player.getPassword()));

        if(isValid(player) == true){
            return playerRepository.save(player);

        }
        return null;
    }
	
	public List<Player> findAll() {
        return playerRepository.findAll();
    }
    public List<Player> findPlayersBySport(String sport) {
        //FIXME RETURNS PASSWORD INFORMATON
        return playerRepository.findPlayersBySport(sport);
    }

    public Principal login(String username, String password){

        String encryptedPassword = passwordUtils.generateSecurePassword(password);
        Player authPlayer = playerRepository.findPlayerByUsernameAndPassword(username, encryptedPassword);

       if(authPlayer == null){
            throw new AuthenticationException("Invalid login credentials");
        }

        return new Principal(authPlayer);
    }

    public Player addTeam(String teamName, String playerUsername)
    {
        Player updateOfferPlayer = playerRepository.findPlayerByUsername(playerUsername);
        updateOfferPlayer.setTeamName(teamName);
        return playerRepository.save(updateOfferPlayer);
    }

    public Player updateOffers(Offer newOffer, String type){
        Player updateOfferPlayer = playerRepository.findPlayerByUsername(newOffer.getPlayerUsername());
        List<String> newList = updateOfferPlayer.getOffers();
        if (type.equals("rescind"))
        {
            while (newList.contains(newOffer.getCoachUsername()))
            {
                newList.remove(newOffer.getCoachUsername());
            }
            updateOfferPlayer.setOffers(newList);
            playerRepository.save(updateOfferPlayer);
        }
        else if (type.equals("extend")) {
            if (!newList.contains(newOffer.getCoachUsername()))
            {
                newList.add(newOffer.getCoachUsername());
            }
            updateOfferPlayer.setOffers(newList);
            playerRepository.save(updateOfferPlayer);
        }
        return updateOfferPlayer;
    }

    public void removeOffer(Offer acceptedOffer) {
        Player removeOfferPlayer = playerRepository.findPlayerByUsername(acceptedOffer.getPlayerUsername());
        List<String> offers = removeOfferPlayer.getOffers();
        boolean removed = offers.remove(acceptedOffer.getCoachUsername());
        if (!removed) {
            throw new InvalidRequestException("You don't have an offer from that coach");
        }
        removeOfferPlayer.setOffers(offers);
        playerRepository.save(removeOfferPlayer);
    }

    public Player getPlayerInfo(String username) {
        Player player = playerRepository.findPlayerByUsername(username);

        if (player == null) { throw new InvalidRequestException("There is no player with that username"); }

        return player;
    }

    public boolean addExercise(String teamPlayer, String exercise) {
        Player currentPlayer = playerRepository.findPlayerByUsername(teamPlayer);
        //Check for duplication?
        List<String> exercises = currentPlayer.getExercises();
        boolean notInList = true;
        for (String e: exercises) {
            if(e.equals(exercise)) {
                notInList = false;
                break;
            }
        }

        if(notInList) {
            exercises.add(exercise);
            currentPlayer.setExercises(exercises);
            playerRepository.save(currentPlayer);
        }

        return notInList;
    }
}
