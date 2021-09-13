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

        if(player.getName() == "" || player.getUsername() == "" || player.getPassword() == "" || player.getPassword().length() <= 7){
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

    public Principal login(String username, String password){

        String encryptedPassword = passwordUtils.generateSecurePassword(password);
        Player authPlayer = playerRepository.findPlayerByUsernameAndPassword(username, encryptedPassword);

        if(authPlayer == null){
            throw new AuthenticationException("Invalid login credentials");
        }

        return new Principal(authPlayer);
    }

    public Player updateOffers(Offer newOffer){

        Player updateOfferPlayer = playerRepository.findPlayerByUsername(newOffer.getPlayerUsername());
        List<String> newList = updateOfferPlayer.getOffers();
        newList.add(newOffer.getCoachUsername());
        updateOfferPlayer.setOffers(newList);
        playerRepository.save(updateOfferPlayer);

        return updateOfferPlayer;
    }

}
