package com.revature.teamManager.services;
import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.documents.Skills;
import com.revature.teamManager.data.repos.PlayerRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.util.exceptions.ResourceNotFoundException;
import com.revature.teamManager.util.exceptions.ResourcePersistenceException;
import com.revature.teamManager.web.dtos.ModifyExercise;
import com.revature.teamManager.web.dtos.Offer;
import com.revature.teamManager.web.dtos.Principal;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PlayerService {

    private PlayerRepository playerRepository;
    private PasswordUtils passwordUtils;

    public PlayerService(PlayerRepository playerRepository, PasswordUtils passwordUtils){
        this.playerRepository = playerRepository;
        this.passwordUtils = passwordUtils;
    }

    //checks if registration input is valid
    public boolean isValid(Player player){
        if(player.getName() == "" || player.getUsername() == "" || player.getPassword() == "" || player.getSports().isEmpty() || player.getPassword().length() <= 7){
            throw new InvalidRequestException("invalid user data");
        }

        if(playerRepository.findPlayerByUsername(player.getUsername()) != null){
            throw new ResourcePersistenceException("username is taken");
        }

        return true;
    }

    //registers a new player
    public Player register(Player player){
        player.setPassword(passwordUtils.generateSecurePassword(player.getPassword()));

        if(isValid(player)){
            return playerRepository.save(player);

        }
        return null;
    }

    //finds all players
	public List<Player> findAll() {
        return playerRepository.findAll();
    }

    //find all players who are interested in a certain sport
    public List<Player> findPlayersBySport(String sport) {
        //FIXME RETURNS PASSWORD INFORMATON
        return playerRepository.findPlayersBySport(sport);
    }

    //login as player
    public Principal login(String username, String password){

        String encryptedPassword = passwordUtils.generateSecurePassword(password);
        Player authPlayer = playerRepository.findPlayerByUsernameAndPassword(username, encryptedPassword);

       if(authPlayer == null){
            throw new AuthenticationException("Invalid login credentials");
        }

        return new Principal(authPlayer);
    }

    //coach adds player to a team
    public void addTeam(String teamName, String playerUsername)
    {
        Player updateOfferPlayer = playerRepository.findPlayerByUsername(playerUsername);
        updateOfferPlayer.setTeamName(teamName);
        playerRepository.save(updateOfferPlayer);
    }

    //coach or player removes a player from a team
    public void removeTeam(String playerUsername) {
        Player updateOfferPlayer = playerRepository.findPlayerByUsername(playerUsername);
        //FIX THIS.. Throw if updateOfferPlayer is null.
        updateOfferPlayer.setTeamName(null);
        playerRepository.save(updateOfferPlayer);
    }

    //coach updates an offer to a player to join a team
    public Player updateOffers(Offer newOffer, String type){
        Player updateOfferPlayer = playerRepository.findPlayerByUsername(newOffer.getPlayerUsername());
        List<String> newList = updateOfferPlayer.getOffers();
        //checks if offer has been rescinded and removes offer if so
        if (type.equals("rescind"))
        {
            while (newList.contains(newOffer.getCoachUsername()))
            {
                newList.remove(newOffer.getCoachUsername());
            }
            updateOfferPlayer.setOffers(newList);
            playerRepository.save(updateOfferPlayer);
        }
        //checks if offer has been extended to player and adds offer to list if so
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

    //add a skill to player profile
    public Player addSkill(String username, String skill){
        Player player = playerRepository.findPlayerByUsername(username);
        if(skillValid(player, skill)){
            Skills skills = new Skills(skill);
            player.getSkills().add(skills);
            playerRepository.save(player);
            return player;
        }
        return null;
    }

    //add a sport to player profile
    public Player addSport(String username, String sport){
        Player player = playerRepository.findPlayerByUsername(username);
        if(sportValid(player, sport)){
            player.getSports().add(sport);
            playerRepository.save(player);
            return player;
        }
        return null;
    }

    //check if data for the skill to be added is valid
    public boolean skillValid(Player player, String skill){

        if (skill == ""){
            throw new InvalidRequestException("Invalid data");
        }
        Player check = playerRepository.findPlayerByUsername(player.getUsername());
        boolean skillExists = check.getSkills().stream().anyMatch(item -> skill.equals(item.getSkill()));
        if (skillExists){
            throw new ResourcePersistenceException("Duplicate data");
        }
        return true;
    }

    //check if data for the sport to be added is valid
    public boolean sportValid(Player player, String sport){
        if (sport == ""){
            throw new InvalidRequestException("Invalid data");
        }
        Player check = playerRepository.findPlayerByUsername(player.getUsername());
        if (check.getSports().contains(sport)){
            throw new ResourcePersistenceException("Duplicate data");
        }
        return true;
    }

    //delete a skill from a player's profile
    public Player deleteSkill(String username, String deleteSkill){
        Player player = playerRepository.findPlayerByUsername(username);
        if(deleteSkillValidation(player, deleteSkill)){
            player.getSkills().removeIf(item -> item.getSkill().equals(deleteSkill));
            playerRepository.save(player);
        }
        return null;
    }

    //check if the skill to be deleted is valid
    public boolean deleteSkillValidation(Player player, String deleteSkill){

        if(deleteSkill == ""){
            throw new InvalidRequestException("Invalid data");
        }
        boolean skillExists = player.getSkills().stream().anyMatch(item -> deleteSkill.equals(item.getSkill()));
        if(!skillExists){
            throw new ResourceNotFoundException();
        }
        return true;
    }

    //delete a sport from a player's profile
    public Player deleteSport(String username, String deleteSport){
        Player player = playerRepository.findPlayerByUsername(username);
        if(deleteSportValidation(player, deleteSport)){
            player.getSports().removeIf(item -> item.equals(deleteSport));
            playerRepository.save(player);
        }
        return null;
    }

    //check if the sport to be deleted is valid
    public boolean deleteSportValidation(Player player, String deleteSport){
        if(deleteSport == ""){
            throw new InvalidRequestException("Invalid data");
        }
        boolean sportExists = player.getSports().stream().anyMatch(item -> deleteSport.equals(item));
        if(!sportExists){
            throw new ResourceNotFoundException();
        }
        return true;
    }

    //coach rescinds a team offer from a player
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

    //returns a player given their username
    public Player getPlayerInfo(String username) {
        Player player = playerRepository.findPlayerByUsername(username);

        if (player == null) { throw new InvalidRequestException("There is no player with that username"); }

        return player;
    }

    //coach adds exercise to a player's profile
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

    //recruiter gives a rating to a player's skill
    public void rateSkill(String username, String skill, int rating) {
        Player toRate = playerRepository.findPlayerByUsername(username);

        if (toRate == null) {
            throw new InvalidRequestException("That player doesn't exist");
        }
        for (int i = 0; i < toRate.getSkills().size(); i++) {
            if (toRate.getSkills().get(i).getSkill().equals(skill)) {
                toRate.getSkills().get(i).setRating(rating);
                playerRepository.save(toRate);
                return;
            }
        }
        throw new InvalidRequestException("That player doesn't have that skill");
    }

    //player specifies if they completed an exercise
    public Player modifyExercise(ModifyExercise exerciseObject, String type){
        Player updateExercisePlayer = playerRepository.findPlayerByUsername(exerciseObject.getPlayerUsername());
        List<String> exercises = updateExercisePlayer.getExercises();
        List<String> completedExercises = updateExercisePlayer.getCompletedExercises();

        //mark exercise as complete if it has been completed
        if (type.equals("complete"))
        {
            while (exercises.contains(exerciseObject.getExercise()))
            {
                exercises.remove(exerciseObject.getExercise());
            }
            completedExercises.add(exerciseObject.getExercise());

            updateExercisePlayer.setExercises(exercises);
            updateExercisePlayer.setCompletedExercises(completedExercises);

            playerRepository.save(updateExercisePlayer);
        }

        //marks exercise as imcomplete if it has not been completed
        else if (type.equals("uncomplete")) {
            while (completedExercises.contains(exerciseObject.getExercise()))
            {
                completedExercises.remove(exerciseObject.getExercise());
            }
            exercises.add(exerciseObject.getExercise());

            updateExercisePlayer.setExercises(exercises);
            updateExercisePlayer.setCompletedExercises(completedExercises);

            playerRepository.save(updateExercisePlayer);
        }
        return updateExercisePlayer;
    }

}
