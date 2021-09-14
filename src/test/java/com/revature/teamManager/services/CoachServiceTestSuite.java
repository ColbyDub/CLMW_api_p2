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
    public void assignPosition_callsRepositoryMethods_whenGivenValidInformation() {
        // Arrange
        String coachUsername = "ValidUsername";
        String playerUsername = "OtherValidUsername";
        String position = "ValidPosition";
        Coach coach = new Coach();
        coach.setUsername("ValidUsername");
        coach.setPassword("ValidPassword");
        coach.setCoachName("ValidCoachName");
        coach.setSport("Soccer");
        coach.setTeamName("ValidTeamName");
        List<String[]> players = new ArrayList<>();
        players.add(new String[] {"HiImBilly", "Forward"});
        coach.setPlayers(players);
        when(mockCoachRepo.findCoachByUsername(any())).thenReturn(coach);
        when(mockCoachRepo.save(any())).thenReturn(null);

        // Act
        sut.assignPosition(coachUsername, playerUsername, position);

        // Assert
        verify(mockCoachRepo, times(1)).findCoachByUsername(any());
        verify(mockCoachRepo, times(1)).save(any());

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
        assertEquals(actualCoach.getPlayers().get(0)[0], updatedCoach.getPlayers().get(0)[0]);

    }

    @Test
    public void assignPosition_throwsInvalidRequestException_whenGivenEmptyCoachUsername() {
        // Arrange
        String coachUsername = "";
        String playerUsername = "OtherValidUsername";
        String position = "ValidPosition";

        // Act
        InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> sut.assignPosition(coachUsername, playerUsername, position));

        // Assert
        assertEquals("You must provide a coach username, player username, and position", ire.getMessage());
        verify(mockCoachRepo, times(0)).findCoachByUsername(any());
        verify(mockCoachRepo, times(0)).save(any());
    }

    @Test
    public void assignPosition_throwsInvalidRequestException_whenGivenEmptyPlayerUsername() {
        // Arrange
        String coachUsername = "ValidUsername";
        String playerUsername = "";
        String position = "ValidPosition";

        // Act
        InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> sut.assignPosition(coachUsername, playerUsername, position));

        // Assert
        assertEquals("You must provide a coach username, player username, and position", ire.getMessage());
        verify(mockCoachRepo, times(0)).findCoachByUsername(any());
        verify(mockCoachRepo, times(0)).save(any());

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

    @Test
    public void getCoach_returnsSuccessfully_whenGivenValidUsername() {
        // Arrange
        String username = "username";
        Coach coach = new Coach();
        coach.setUsername("username");
        coach.setCoachName("Name");
        coach.setPassword("password");
        coach.setSport("sport");
        coach.setTeamName("Go Team");
        when(mockCoachRepo.findCoachByUsername(any())).thenReturn(coach);

        // Act
        Coach actualCoach = sut.getCoach(username);

        // Assert
        assertEquals(coach, actualCoach);
        verify(mockCoachRepo, times(1)).findCoachByUsername(any());

    }

    @Test
    public void getCoach_throwsInvalidRequestException_whenUsernameNotInDb() {
        // Arrange
        String username = "invalidUsername";
        when(mockCoachRepo.findCoachByUsername(any())).thenReturn(null);

        // Act
        InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> sut.getCoach(username));

        // Assert
        verify(mockCoachRepo,times(1)).findCoachByUsername(any());

    }

    @Test
    public void getTeamForPlayer_returnsSuccessfully_whenGivenValidUsername() {
        // Arrange
        String username = "validUsername";
        Coach coach = new Coach();
        coach.setCoachName("Bob Bobson");
        coach.setUsername("Bobby");
        coach.setPassword("password");
        coach.setTeamName("Springs");
        coach.setSport("Basketball");
        List<String[]> players = new ArrayList<>();
        players.add(new String[] {"validUsername", "No Position"});
        coach.setPlayers(players);
        when(mockCoachRepo.findCoachByPlayersContaining(any())).thenReturn(coach);

        // Act
        Coach found = sut.getTeamForPlayer(username);

        // Assert
        assertEquals(username, found.getPlayers().get(0)[0]);
        verify(mockCoachRepo,times(1)).findCoachByPlayersContaining(any());
    }

    @Test
    public void getTeamForPlayer_throwsInvalidRequestException_whenGivenInvalidUsername() {
        // Arrange
        String username = "invalidUsername";
        when(mockCoachRepo.findCoachByPlayersContaining(any())).thenReturn(null);

        // Act
        InvalidRequestException ire = assertThrows(InvalidRequestException.class, () -> sut.getTeamForPlayer(username));

        // Assert
        assertEquals(ire.getMessage(), "You aren't on a team");
        verify(mockCoachRepo,times(1)).findCoachByPlayersContaining(any());
    }
}
