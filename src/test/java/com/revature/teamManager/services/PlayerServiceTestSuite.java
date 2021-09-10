package com.revature.teamManager.services;

import ch.qos.logback.core.spi.FilterReply;
import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.repos.PlayerRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.util.exceptions.ResourcePersistenceException;
import com.revature.teamManager.web.dtos.Principal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Player player = new Player("name", "username", "password");
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
        Player player = new Player("", "username", "password");

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(player));

        assertEquals("invalid user data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsInvalidRequestException_whenUsernameIsBlank(){
        //arrange
        Player player = new Player("name", "", "password");

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(player));

        assertEquals("invalid user data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsInvalidRequestException_whenPasswordIsBlank(){
        //arrange
        Player player = new Player("name", "username", "");

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(player));

        assertEquals("invalid user data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsInvalidRequestException_whenPasswordIsSevenCharacters(){
        //arrange
        Player player = new Player("name", "username", "1234567");

        //act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(player));

        assertEquals("invalid user data", e.getMessage());

        verify(mockPlayerRepo, times(0)).findPlayerByUsername(any());

    }

    @Test()
    public void isValid_throwsResourcePersistenceException_whenUsernameIsTaken(){
        //arrange
        Player player = new Player("name", "takenUsername", "password");
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
        Player player = new Player("name", "username", "password");
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

}