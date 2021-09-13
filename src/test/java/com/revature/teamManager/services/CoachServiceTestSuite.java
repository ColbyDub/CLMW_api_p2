package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.repos.CoachRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
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

public class CoachServiceTestSuite {
    CoachService sut;

    private CoachRepository mockCoachRepo;
    private PasswordUtils passwordUtils;

    @BeforeEach
    public void beforeEachTest() {
        mockCoachRepo = mock(CoachRepository.class);
        passwordUtils = mock(PasswordUtils.class);
        sut = new CoachService(mockCoachRepo, passwordUtils);
    }

    @AfterEach
    public void afterEachTest() {
        sut = null;
    }

    @Test
    public void isValid_returnsTrue_whenGivenValidCoach() {
        // arrange
        Coach validCoach = new Coach();
        validCoach.setCoachName("Bob");
        validCoach.setUsername("Bobby");
        validCoach.setPassword("password");
        validCoach.setSport("Basketball");
        validCoach.setTeamName("Fighting TypeScripts");
        when(mockCoachRepo.findCoachByUsername(any())).thenReturn(null);

        // act
        boolean actualResult = sut.isValid(validCoach);

        // assert
        assertTrue("Expected coach to be considered valid",actualResult);
        verify(mockCoachRepo,times(1)).findCoachByUsername(any());
    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenEmptyName() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act

        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidCoach));

        assertEquals("You must provide all necessary information", e.getMessage());

        verify(mockCoachRepo, times(0)).findCoachByUsername(any());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenEmptyUsername() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act

        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidCoach));

        assertEquals("You must provide all necessary information", e.getMessage());

        // assert
        verify(mockCoachRepo,times(0)).findCoachByUsername(any());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenAlreadyExistingUsername() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("ExistingUsername");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");
        when(mockCoachRepo.findCoachByUsername(any())).thenReturn(invalidCoach);

        // act

        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidCoach));

        assertEquals("That username is already taken", e.getMessage());

            // assert
            verify(mockCoachRepo, times(1)).findCoachByUsername(invalidCoach.getUsername());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenEmptyPassword() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidCoach));

        assertEquals("You must provide all necessary information", e.getMessage());

        verify(mockCoachRepo,times(0)).findCoachByUsername(any());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenPasswordLessThan8Characters() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("pass");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidCoach));

        assertEquals("Your password must be at least 8 characters long", e.getMessage());

        // assert
        verify(mockCoachRepo,times(0)).findCoachByUsername(any());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenEmptySport() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidCoach));

        assertEquals("You must provide all necessary information", e.getMessage());

        // assert
        verify(mockCoachRepo,times(0)).findCoachByUsername(any());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenEmptyTeamName() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("");

        // act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidCoach));

        assertEquals("You must provide all necessary information", e.getMessage());

        // assert
        verify(mockCoachRepo,times(0)).findCoachByUsername(any());

    }

    @Test
    public void register_returnsSuccessfully_whenGivenValidCoach() {
        // arrange
        Coach validCoach = new Coach();
        validCoach.setCoachName("Bob");
        validCoach.setUsername("Bobby");
        validCoach.setPassword("password");
        validCoach.setSport("Basketball");
        validCoach.setTeamName("Fighting TypeScripts");
        when(mockCoachRepo.save(any())).thenReturn(validCoach);
        when(mockCoachRepo.findCoachByUsername(any())).thenReturn(null);

        // act
        Coach actualResult = sut.register(validCoach);

        // assert
        verify(mockCoachRepo, times(1)).save(any());
        assertEquals(validCoach,actualResult);
    }

    @Test
    public void login_returnsSuccessfully_whenGivenValidCredentials() {
        // Arrange
        Coach validCoach = new Coach();
        validCoach.setUsername("valid");
        validCoach.setPassword("valid");
        validCoach.setCoachName("valid");
        validCoach.setSport("valid");
        validCoach.setTeamName("valid");

        Principal expectedResult = new Principal();
        expectedResult.setUsername("valid");
        expectedResult.setRole("Coach");

        when(mockCoachRepo.findCoachByUsernameAndPassword(any(), any())).thenReturn(validCoach);

        // Act
        Principal actualResult = sut.login("valid", "valid");

        // Assert
        assertEquals(expectedResult, actualResult);
        verify(mockCoachRepo, times(1)).findCoachByUsernameAndPassword(any(), any());
    }

    @Test
    public void login_throwsAuthenticationException_whenGivenInvalidCredentials() {
        // Arrange
        when(mockCoachRepo.findCoachByUsernameAndPassword(any(),any())).thenReturn(null);

        // Act
        AuthenticationException ae = assertThrows(AuthenticationException.class, () -> sut.login("invalid", "invalid"));

        // Assert
        assertEquals("Invalid username/password combo", ae.getMessage());
        verify(mockCoachRepo, times(1)).findCoachByUsernameAndPassword(any(), any());
    }

    @Test
    void offer_returnsSuccessful_whenValidCoachAndPlayer(){

    }

    @Test
    public void addPlayer_returnsSuccessfully_WhenGivenUsernameAndPassword() {
        // Arrange
        Coach coach = new Coach();
        coach.setCoachName("Bob Bobson");
        coach.setUsername("Bobby");
        coach.setPassword("password");
        coach.setTeamName("Springs");
        coach.setSport("Basketball");
        when(mockCoachRepo.findCoachByUsername(any())).thenReturn(coach);

        Coach updatedCoach = new Coach();
        updatedCoach.setCoachName("Bob Bobson");
        updatedCoach.setUsername("Bobby");
        updatedCoach.setPassword("password");
        updatedCoach.setTeamName("Springs");
        updatedCoach.setSport("Basketball");
        List<String[]> players = new ArrayList<>();
        players.add(new String[] {"Billy", "No Position"});
        updatedCoach.setPlayers(players);
        when(mockCoachRepo.save(any())).thenReturn(null);

        // Act
        Coach actualCoach = sut.addPlayer("Bobby", "Billy");

        // Assert
        verify(mockCoachRepo, times(1)).findCoachByUsername(any());
        verify(mockCoachRepo, times(1)).save(any());
        assertEquals(actualCoach, updatedCoach);
    }

    @Test
    public void addPlayer_doesNotAddPlayer_ifPlayerAlreadyOnTeam() {
        // Arrange
        Coach coach = new Coach();
        coach.setCoachName("Bob Bobson");
        coach.setUsername("Bobby");
        coach.setPassword("password");
        coach.setTeamName("Springs");
        coach.setSport("Basketball");
        List<String[]> players = new ArrayList<>();
        players.add(new String[] {"Billy", "No Position"});
        coach.setPlayers(players);
        when(mockCoachRepo.findCoachByUsername(any())).thenReturn(coach);

        // Act
        Coach actualCoach = sut.addPlayer("Bobby", "Billy");

        // Assert
        verify(mockCoachRepo, times(1)).findCoachByUsername(any());
        verify(mockCoachRepo, times(0)).save(any());
        assertEquals(actualCoach, coach);
    }
}
