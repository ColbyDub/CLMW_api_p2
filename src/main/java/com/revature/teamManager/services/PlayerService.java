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

    public boolean isValid(Player player){

        if(player.getName() == "" || player.getUsername() == "" || player.getPassword() == "" || player.getSports().isEmpty() || player.getPassword().length() <= 7){
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

    public void addTeam(String teamName, String playerUsername)
    {
        Player updateOfferPlayer = playerRepository.findPlayerByUsername(playerUsername);
        updateOfferPlayer.setTeamName(teamName);
        playerRepository.save(updateOfferPlayer);
    }

    public void removeTeam(String playerUsername) {
        Player updateOfferPlayer = playerRepository.findPlayerByUsername(playerUsername);
        //FIX THIS.. Throw if updateOfferPlayer is null.
        updateOfferPlayer.setTeamName(null);
        playerRepository.save(updateOfferPlayer);
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

    public Player addSport(String username, String sport){
        Player player = playerRepository.findPlayerByUsername(username);
        if(sportValid(player, sport)){
            player.getSports().add(sport);
            playerRepository.save(player);
            return player;
        }
        return null;
    }

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

    public Player deleteSkill(String username, String deleteSkill){
        Player player = playerRepository.findPlayerByUsername(username);
        if(deleteSkillValidation(player, deleteSkill)){
            player.getSkills().removeIf(item -> item.getSkill().equals(deleteSkill));
            playerRepository.save(player);
        }
        return null;
    }

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

    public Player deleteSport(String username, String deleteSport){
        Player player = playerRepository.findPlayerByUsername(username);
        if(deleteSportValidation(player, deleteSport)){
            player.getSports().removeIf(item -> item.equals(deleteSport));
            playerRepository.save(player);
        }
        return null;
    }

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

    public Player modifyExercise(ModifyExercise exerciseObject, String type){
        Player updateExercisePlayer = playerRepository.findPlayerByUsername(exerciseObject.getPlayerUsername());
        List<String> exercises = updateExercisePlayer.getExercises();
        List<String> completedExercises = updateExercisePlayer.getCompletedExercises();
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
