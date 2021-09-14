package com.revature.teamManager.services;

import ch.qos.logback.core.spi.FilterReply;
import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.documents.Skills;
import com.revature.teamManager.data.repos.PlayerRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.util.exceptions.ResourcePersistenceException;
import com.revature.teamManager.web.dtos.Offer;
import com.revature.teamManager.web.dtos.Principal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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

    //validSports tests
    @Test
    public void sportValid_returnsTrue_whenGivenValidValue(){
        Player player = new Player("name", "username", "password");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String addSport = "baseball";

        boolean result = sut.sportValid(player, addSport);

        assertTrue("Expect to be valid", result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
    }

    @Test
    public void sportValid_throwsInvalidRequestException_whenGivenEmptyValue(){

        Player player = new Player("name", "username", "password");
        String addSport = "";

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.sportValid(player, addSport));

        assertEquals("Invalid data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());
    }

    @Test
    public void sportValid_throwsResourcePersistenceException_whenGivenDuplicateValue(){

        Player player = new Player("name", "username", "password");
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

        Player player = new Player("name", "username", "password");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String addSport = "baseball";

        Player result = sut.addSport("username", addSport);

        assertEquals(player, result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());
    }

    //validSkills tests
    @Test
    public void skillValid_returnsTrue_whenGivenValidValue(){

        Player player = new Player("name", "username", "password");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String addSkill = "bunting";

        boolean result = sut.skillValid(player, addSkill);

        assertTrue("Expect to be valid", result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
    }

    @Test
    public void skillValid_throwsInvalidRequestException_whenGivenEmptyValue(){

        Player player = new Player("name", "username", "password");
        String addSkill = "";

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.skillValid(player, addSkill));

        assertEquals("Invalid data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());
    }

    @Test
    public void skillValid_throwsResourcePersistenceException_whenGivenDuplicateValue(){

        Player player = new Player("name", "username", "password");
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
    public void addSkill_returnsPlayer_whenGivenValidValue(){

        Player player = new Player("name", "username", "password");
        when(mockPlayerRepo.findPlayerByUsername(any())).thenReturn(player);
        String addSkill = "baseball";

        Player result = sut.addSkill("username", addSkill);

        assertEquals(player, result);
        verify(mockPlayerRepo, times(1)).findPlayerByUsername(any());
        verify(mockPlayerRepo, times(1)).save(any());
    }

}