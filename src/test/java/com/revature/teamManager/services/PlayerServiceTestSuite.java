package com.revature.teamManager.services;

import ch.qos.logback.core.spi.FilterReply;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.ArrayList;
import java.util.List;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class PlayerServiceTestSuite {
    PlayerService sut;

    private PlayerRepository mockPlayerRepo;
    private PasswordUtils mockPasswordUtils;

    @BeforeEach
    public void beforeEachTest(){
        mockPlayerRepo = mock(PlayerRepository.class);
        mockPasswordUtils = mock(PasswordUtils.class);
        sut = new PlayerService(mockPlayerRepo, mockPasswordUtils);
    }

    @AfterEach
    public void afterEachTest(){
        sut = null;
    }


    //isValid tests
    @Test
    public void isValid_returnsTrue_whenGivenValidValues(){
        //arrange
        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(null);

        //act
        boolean result = sut.isValid(player);

        //assert
        assertTrue("expect to be valid", result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsInvalidRequestException_whenNameIsBlank(){
        //arrange
        Player player = new Player("", "username", "password", "sport");

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(player));

        assertEquals("invalid user data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsInvalidRequestException_whenUsernameIsBlank(){
        //arrange
        Player player = new Player("name", "", "password", "sport");

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(player));

        assertEquals("invalid user data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsInvalidRequestException_whenPasswordIsBlank(){
        //arrange
        Player player = new Player("name", "username", "", "sport");

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(player));

        assertEquals("invalid user data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsInvalidRequestException_whenPasswordIsSevenCharacters(){
        //arrange
        Player player = new Player("name", "username", "1234567", "sport");

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(player));

        assertEquals("invalid user data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsResourcePersistenceException_whenUsernameIsTaken(){
        //arrange
        Player player = new Player("name", "takenUsername", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);

        //act
        ResourcePersistenceException e = assertThrows(ResourcePersistenceException.class, () -> sut.isValid(player));

        assertEquals("username is taken", e.getMessage());

        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());

    }


    //register tests
    @Test()
    public void register_returnsPlayerAndCallsBothApplicableRepoMethods_whenGivenValidValues(){
        //arrange
        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(null);
        when(mockPlayerRepo.save(any())).thenReturn(player);
        when(mockPasswordUtils.generateSecurePassword(any())).thenReturn("password");

        //act
        Player result = sut.register(player);

        //assert
        assertEquals(player, result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());
        verify(mockPasswordUtils, times(1)).generateSecurePassword( any());

    }

    //login tests
    @Test
    public void login_returnsPrinciple_whenGivenValidData(){
        Player player = new Player();
        Principal principal = new Principal(player);
        when(mockPlayerRepo.findPlayerByUsernameAndPassword(any(), any())).thenReturn(player);
        when(mockPasswordUtils.generateSecurePassword(any())).thenReturn("password");

        Principal result = sut.login("username", "password");

        assertEquals(principal, result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsernameAndPassword(any(), any());
        verify(mockPasswordUtils, times(1)).generateSecurePassword( any());
    }

    @Test
    public void login_throwsAuthenticationException_whenGivenInvalidValues(){

        Player player = new Player();
        when(mockPlayerRepo.findPlayerByUsernameAndPassword(any(), any())).thenReturn(null);
        when(mockPasswordUtils.generateSecurePassword(any())).thenReturn("password");

        AuthenticationException e = assertThrows(AuthenticationException.class, () -> sut.login("username", "password"));

        assertEquals("Invalid login credentials", e.getMessage());
        verify(mockPlayerRepo, times(1)).findPlayerByUsernameAndPassword(any(), any());
        verify(mockPasswordUtils, times(1)).generateSecurePassword( any());
    }

    //validSports tests
    @Test
    public void sportValid_returnsTrue_whenGivenValidValue(){
        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String addSport = "baseball";

        boolean result = sut.sportValid(player, addSport);

        assertTrue("Expect to be valid", result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
    }

    @Test
    public void sportValid_throwsInvalidRequestException_whenGivenEmptyValue(){

        Player player = new Player("name", "username", "password", "sport");
        String addSport = "";

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.sportValid(player, addSport));

        assertEquals("Invalid data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());
    }

    @Test
    public void sportValid_throwsResourcePersistenceException_whenGivenDuplicateValue(){

        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        player.getSports().add("baseball");
        String addSport = "baseball";

        ResourcePersistenceException e = assertThrows(ResourcePersistenceException.class, () -> sut.sportValid(player, addSport));

        assertEquals("Duplicate data", e.getMessage());

        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
    }

    //addSport
    @Test
    public void addSport_returnsPlayer_whenGivenValidValue(){

        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String addSport = "baseball";

        Player result = sut.addSport("username", addSport);

        assertEquals(player, result);
        verify(mockPlayerRepo, times(2)).findPlayerByUsername(any()); //addSkill calls skillValid, both call find
        verify(mockPlayerRepo, times(1)).save(any());
    }

    //validSkills tests
    @Test
    public void skillValid_returnsTrue_whenGivenValidValue(){

        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String addSkill = "bunting";

        boolean result = sut.skillValid(player, addSkill);

        assertTrue("Expect to be valid", result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
    }

    @Test
    public void skillValid_throwsInvalidRequestException_whenGivenEmptyValue(){

        Player player = new Player("name", "username", "password", "sport");
        String addSkill = "";

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.skillValid(player, addSkill));

        assertEquals("Invalid data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());
    }

    @Test
    public void skillValid_throwsResourcePersistenceException_whenGivenDuplicateValue(){

        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        Skills skill = new Skills("bunting");
        player.getSkills().add(skill);
        String addSkill = "bunting";

        ResourcePersistenceException e = assertThrows(ResourcePersistenceException.class, () -> sut.skillValid(player, addSkill));

        assertEquals("Duplicate data", e.getMessage());

        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
    }

    //addSkill
    @Test
    public void addSkill_returnsPlayer_whenGivenValidValue() {

        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String addSkill = "baseball";

        Player result = sut.addSkill("username", addSkill);

        assertEquals(player, result);
        verify(mockPlayerRepo, times(2)).findPlayerByUsername(any()); //addSkill calls skillValid, both call find
        verify(mockPlayerRepo, times(1)).save(any());
    }

    //deleteSkill
    @Test
    public void deleteSkill_withOneSkillInListReturnsPlayerWithEmptySkills_whenGivenValidValue(){
        Player player = new Player("name", "username", "password", "sport");
        Skills skill = new Skills("running");
        player.getSkills().add(skill);
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String deleteSkill = "running";

        sut.deleteSkill("username", deleteSkill);

        assertTrue("empty skills", player.getSkills().isEmpty());
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());
    }

    @Test
    public void deleteSkillValidation_throwsResourceNotFoundException_whenGivenNonExistentValue(){
        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String deleteSkill = "running";

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> sut.deleteSkillValidation(player, deleteSkill));

        assertEquals("No resource found using provided search criteria.", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());
    }

    @Test
    public void deleteSkillValidation_throwsInvalidRequestException_whenGivenEmptyValue(){

        Player player = new Player("name", "username", "password", "sport");
        String deleteSkill = "";

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.deleteSkillValidation(player, deleteSkill));

        assertEquals("Invalid data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());
    }

    //deleteSport
    @Test
    public void deleteSport_withOneSportInListReturnsPlayerWithEmptySports_whenGivenValidValue(){
        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String deleteSport = "sport";

        sut.deleteSport("username", deleteSport);

        assertTrue("empty sports", player.getSports().isEmpty());
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());
    }

    @Test
    public void deleteSportValidation_throwsResourceNotFoundException_whenGivenNonExistentValue(){
        Player player = new Player("name", "username", "password", "sport");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String deleteSport = "basketball";

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> sut.deleteSportValidation(player, deleteSport));

        assertEquals("No resource found using provided search criteria.", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());
    }

    @Test
    public void deleteSportValidation_throwsInvalidRequestException_whenGivenEmptyValue(){

        Player player = new Player("name", "username", "password", "sport");
        String deleteSport = "";

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.deleteSportValidation(player, deleteSport));

        assertEquals("Invalid data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());
    }

    @Test
    public void removeOffer_updatesPlayerOffers_whenGivenValidInformation() {
        // Arrange
        Offer validOffer = new Offer();
        validOffer.setCoachUsername("validCoach");
        validOffer.setPlayerUsername("validPlayer");
        Player player = new Player();
        player.setName("Billy");
        player.setUsername("validPlayer");
        player.setPassword("password");
        List<String> offers = new ArrayList<>();
        offers.add("validCoach");
        player.setOffers(offers);
        when(mockPlayerRepo.findPlayerByUsername("validPlayer")).thenReturn(player);
        when(mockPlayerRepo.save(any())).thenReturn(null);


        // Act
        sut.removeOffer(validOffer);

        // Assert
        verify(mockPlayerRepo, times(1)).findPlayerByUsername("validPlayer");
        verify(mockPlayerRepo, times(1)).save(any());
    }

    @Test
    public void removeOffer_throwsInvalidRequestException_whenGivenInvalidOffer() {
        // Arrange
        Offer invalidOffer = new Offer();
        invalidOffer.setCoachUsername("validCoach");
        invalidOffer.setPlayerUsername("validPlayer");
        Player player = new Player();
        player.setName("Billy");
        player.setUsername("validPlayer");
        player.setPassword("password");
        when(mockPlayerRepo.findPlayerByUsername("validPlayer")).thenReturn(player);

        // Act
        InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> sut.removeOffer(invalidOffer));

        // Assert
        assertEquals(ire.getMessage(), "You don't have an offer from that coach");
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(0)).save(any());
    }

    @Test
    public void getPlayerInfo_ReturnsSuccessfully_whenGivenValidUsername() {
        // Arrange
        String username = "validUsername";
        Player player = new Player();
        player.setName("Bob Bobson");
        player.setUsername("validUsername");
        player.setPassword("password");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);

        // Act
        Player actualPlayer = sut.getPlayerInfo(username);

        //Assert
        assertEquals(player,actualPlayer);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());

    }

    @Test
    public void getPlayerInfo_throwsInvalidRequestException_whenGivenInvalidUsername() {
        // Arrange
        String username = "invalidUsername";
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(null);

        // Act
        InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> sut.getPlayerInfo(username));

        // Assert
        assertEquals(ire.getMessage(), "There is no player with that username");
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
    }

    @Test
    public void addExercise_returnsTrue_WhenExercisesDontHaveAddedOne(){
        Player player = new Player();
        player.setName("Bob Bobson");
        player.setUsername("validUsername");
        player.setPassword("password");

        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);

        boolean check = sut.addExercise("validUsername","Rope jumps");

        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());

        assertTrue(null,check);

    }

    @Test
    public void addExercise_returnsFalse_WhenExercisesContainAddedOne(){
        Player player = new Player();
        player.setName("Bob Bobson");
        player.setUsername("validUsername");
        player.setPassword("password");
        List<String> exercises = new ArrayList<>();
        exercises.add("Rump Jope");
        player.setExercises(exercises);

        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);

        boolean check = sut.addExercise("validUsername","Rump Jope");

        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());

        assertFalse(check);
    }

    @Test
    public void rateSkill_updatesRepository_whenGivenValidInformation() {
        // Arrange
        Player player = new Player();
        player.setName("Billy Bobson");
        player.setUsername("HiImBilly");
        player.setPassword("password");
        Skills skill = new Skills("Jumping");
        List<Skills> setThis = new ArrayList<>();
        setThis.add(skill);
        player.setSkills(setThis);
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        when(mockPlayerRepo.save(any())).thenReturn(null);

        // Act
        sut.rateSkill("HiImBilly", "Jumping", 5);

        // Assert
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        Skills newSkill = new Skills("Jumping");
        newSkill.setRating(5);
        List<Skills> noSetThis = new ArrayList<>();
        noSetThis.add(newSkill);
        player.setSkills(noSetThis);
        verify(mockPlayerRepo, times(1)).save(player);

    }

    @Test
    public void rateSkill_throwsInvalidRequestException_whenGivenInvalidUsername() {
        // Arrange
        String username = "billybob";
        String skill = "sprint";
        int rating = 2;
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(null);

        // Act
        InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> sut.rateSkill(username, skill, rating));

        // Assert
        assertEquals(ire.getMessage(), "That player doesn't exist");
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(0)).save(any());
    }

    @Test
    public void rateSkill_throwsInvalidRequestException_whenGivenInvalidSkill() {
        // Arrange
        Player player = new Player();
        player.setName("Billy Bobson");
        player.setUsername("HiImBilly");
        player.setPassword("password");
        Skills skill = new Skills("Jumping");
        List<Skills> setThis = new ArrayList<>();
        setThis.add(skill);
        player.setSkills(setThis);
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);

        // Act
        InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> sut.rateSkill("HiImBilly", "sprint", 4));

        // Assert
        assertEquals(ire.getMessage(), "That player doesn't have that skill");
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(0)).save(any());
    }

    @Test
    public void updateOffers_extendsOffer_whenGivenValidOfferAndType() {
        // Arrange
        Player player = new Player();
        player.setName("name");
        player.setUsername("username");
        player.setPassword("password");

        Player updatedPlayer = new Player();
        updatedPlayer.setName("name");
        updatedPlayer.setUsername("username");
        updatedPlayer.setPassword("password");
        List<String> playerOffers = new ArrayList<>();
        playerOffers.add("coach");
        updatedPlayer.setOffers(playerOffers);

        Offer newOffer = new Offer();
        newOffer.setPlayerUsername("username");
        newOffer.setCoachUsername("coach");

        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        when(mockPlayerRepo.save(any())).thenReturn(null);

        // Act
        Player actualUpdatedPlayer = sut.updateOffers(newOffer, "extend");

        // Assert
        assertEquals(actualUpdatedPlayer.toString(), updatedPlayer.toString());
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());

    }

    @Test
    public void updateOffers_removesOffer_whenGivenValidOfferAndType() {
        // Arrange
        Player player = new Player();
        player.setName("name");
        player.setUsername("username");
        player.setPassword("password");
        List<String> playerOffers = new ArrayList<>();
        playerOffers.add("coach");
        player.setOffers(playerOffers);

        Player updatedPlayer = new Player();
        updatedPlayer.setName("name");
        updatedPlayer.setUsername("username");
        updatedPlayer.setPassword("password");

        Offer newOffer = new Offer();
        newOffer.setPlayerUsername("username");
        newOffer.setCoachUsername("coach");

        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        when(mockPlayerRepo.save(any())).thenReturn(null);

        // Act
        Player actualUpdatedPlayer = sut.updateOffers(newOffer, "rescind");

        // Assert
        assertEquals(actualUpdatedPlayer.toString(), updatedPlayer.toString());
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());

    }

    @Test
    public void modifyExercise_completesExercise_whenGivenValidExerciseAndType() {
        // Arrange
        List<String> exercises = new ArrayList<>();
        exercises.add("exercise");

        Player player = new Player();
        player.setName("name");
        player.setUsername("username");
        player.setPassword("password");
        player.setExercises(exercises);

        Player updatedPlayer = new Player();
        updatedPlayer.setName("name");
        updatedPlayer.setUsername("username");
        updatedPlayer.setPassword("password");
        updatedPlayer.setCompletedExercises(exercises);

        ModifyExercise input = new ModifyExercise();
        input.setPlayerUsername("username");
        input.setExercise("exercise");

        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        when(mockPlayerRepo.save(any())).thenReturn(null);

        // Act
        Player actualUpdatedPlayer = sut.modifyExercise(input, "complete");

        // Assert
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());

    }

    @Test
    public void modifyExercises_uncompletesExercise_whenGivenValidExerciseAndType() {
        // Arrange
        List<String> exercises = new ArrayList<>();
        exercises.add("exercise");

        Player player = new Player();
        player.setName("name");
        player.setUsername("username");
        player.setPassword("password");
        player.setExercises(new ArrayList<>());
        player.setCompletedExercises(exercises);

        Player updatedPlayer = new Player();
        updatedPlayer.setName("name");
        updatedPlayer.setUsername("username");
        updatedPlayer.setPassword("password");
        updatedPlayer.setExercises(exercises);

        ModifyExercise input = new ModifyExercise();
        input.setPlayerUsername("username");
        input.setExercise("exercise");

        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        when(mockPlayerRepo.save(any())).thenReturn(null);

        // Act
        Player completedPlayer = sut.modifyExercise(input, "uncomplete");

        // Assert
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());

    }

}