package com.revature.teamManager.services;
import com.revature.teamManager.data.documents.Pin;
import com.revature.teamManager.data.documents.Recruiter;
import com.revature.teamManager.data.repos.PinRepository;
import com.revature.teamManager.data.repos.RecruiterRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.web.dtos.Principal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class RecruiterServiceTestSuite {

    RecruiterService sut;

    private RecruiterRepository mockRecruiterRepo;
    private PinRepository mockPinRepo;
    private PasswordUtils passwordUtils;

    @BeforeEach
    public void beforeEachTest(){
        mockRecruiterRepo = mock(RecruiterRepository.class);
        mockPinRepo = mock(PinRepository.class);
        passwordUtils = mock(PasswordUtils.class);

        sut = new RecruiterService(mockRecruiterRepo, mockPinRepo, passwordUtils);
    }

    @AfterEach
    public void afterEachTest(){
        sut = null;
    }

    @Test
    public void isValid_returnsTrue_whenGivenValidCourse() {
        // arrange
        Recruiter validRecruiter = new Recruiter();
        validRecruiter.setName("Bob");
        validRecruiter.setUsername("Bobby");
        validRecruiter.setPassword("password");

        when(mockRecruiterRepo.findRecruiterByUsername(any())).thenReturn(null);

        // act
        boolean actualResult = sut.isValid(validRecruiter);

        // assert
        assertTrue("Expected coach to be considered valid",actualResult);
        verify(mockRecruiterRepo,times(1)).findRecruiterByUsername(any());
    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenEmptyName() {
        // arrange
        Recruiter invalidRecruiter = new Recruiter();
        invalidRecruiter.setName("");
        invalidRecruiter.setUsername("Bobby");
        invalidRecruiter.setPassword("password");

        // act

        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidRecruiter));

        assertEquals("Name cannot be left blank", e.getMessage());

        verify(mockRecruiterRepo, times(0)).findRecruiterByUsername(any());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenEmptyUsername() {
        // arrange
        Recruiter invalidRecruiter = new Recruiter();
        invalidRecruiter.setName("Bob");
        invalidRecruiter.setUsername("");
        invalidRecruiter.setPassword("password");

        // act

        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidRecruiter));

        assertEquals("Username cannot be left blank", e.getMessage());

        // assert
        verify(mockRecruiterRepo,times(0)).findRecruiterByUsername(any());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenAlreadyExistingUsername() {
        // arrange
        Recruiter invalidRecruiter = new Recruiter();
        invalidRecruiter.setName("Bob");
        invalidRecruiter.setUsername("ExistingUsername");
        invalidRecruiter.setPassword("password");

        when(mockRecruiterRepo.findRecruiterByUsername(any())).thenReturn(invalidRecruiter);

        // act

        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidRecruiter));

        assertEquals("That username is already taken", e.getMessage());

        // assert
        verify(mockRecruiterRepo, times(1)).findRecruiterByUsername(invalidRecruiter.getUsername());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenEmptyPassword() {
        // arrange
        Recruiter invalidRecruiter = new Recruiter();
        invalidRecruiter.setName("Bob");
        invalidRecruiter.setUsername("Bobby");
        invalidRecruiter.setPassword("");

        // act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidRecruiter));

        assertEquals("Your password must be at least 8 characters long", e.getMessage());

        verify(mockRecruiterRepo,times(0)).findRecruiterByUsername(any());

    }

    @Test
    public void isValid_throwsInvalidRequestException_whenGivenPasswordLessThan8Characters() {
        // arrange
        Recruiter invalidRecruiter = new Recruiter();
        invalidRecruiter.setName("Bob");
        invalidRecruiter.setUsername("Bobby");
        invalidRecruiter.setPassword("pass");

        // act
        InvalidRequestException e = assertThrows(InvalidRequestException.class, () -> sut.isValid(invalidRecruiter));

        assertEquals("Your password must be at least 8 characters long", e.getMessage());

        // assert
        verify(mockRecruiterRepo,times(0)).findRecruiterByUsername(any());

    }

    @Test
    public void register_throwsAuthenticationException_whenGivenInvalidPin() {
        // arrange
        Recruiter validRecruiter = new Recruiter();
        validRecruiter.setName("Bob");
        validRecruiter.setUsername("Bobby");
        validRecruiter.setPassword("password");

        when(mockRecruiterRepo.save(any())).thenReturn(validRecruiter);
        when(mockRecruiterRepo.findRecruiterByUsername(any())).thenReturn(null);
        when(passwordUtils.generateSecurePin(anyString())).thenReturn("invalid");

        // act
        AuthenticationException ae = assertThrows(AuthenticationException.class, () -> sut.register(validRecruiter,"invalid"));

        // Assert
        assertEquals("Invalid Pin", ae.getMessage());
        verify(passwordUtils, times(1)).generateSecurePin(anyString());
        verify(mockPinRepo, times(1)).findPinByEncryptedPin(anyString());
    }


    @Test
    public void register_returnsSuccessfully_whenGivenValidRecruiter() {
        // arrange
        Recruiter validRecruiter = new Recruiter();
        validRecruiter.setName("Bob");
        validRecruiter.setUsername("Bobby");
        validRecruiter.setPassword("password");

        when(mockRecruiterRepo.save(any())).thenReturn(validRecruiter);
        when(mockRecruiterRepo.findRecruiterByUsername(any())).thenReturn(null);
        when(mockPinRepo.findPinByEncryptedPin(anyString())).thenReturn(new Pin("recruiter","any"));
        when(passwordUtils.generateSecurePin(anyString())).thenReturn("any");

        // act
        Recruiter actualResult = sut.register(validRecruiter, "valid pin");

        // assert
        verify(mockPinRepo, times(1)).findPinByEncryptedPin(any());
        verify(mockRecruiterRepo, times(1)).save(any());
        verify(passwordUtils,times(1)).generateSecurePin(anyString());
        verify(passwordUtils,times(1)).generateSecurePassword(any());
        assertEquals(validRecruiter,actualResult);
    }


    @Test
    public void login_returnsPrincipal_whenGivenValidRecruiter() {
        // arrange
        Recruiter validRecruiter = new Recruiter();
        validRecruiter.setName("Bob");
        validRecruiter.setUsername("Bobby");
        validRecruiter.setPassword("password");

        when(mockRecruiterRepo.findRecruiterByUsernameAndPassword(any(),any())).thenReturn(validRecruiter);
        when(passwordUtils.generateSecurePassword(any())).thenReturn("password");

        // act
        Principal actualResult = sut.login(validRecruiter.getUsername(),validRecruiter.getPassword());

        // assert
        verify(passwordUtils, times(1)).generateSecurePassword(any());
        verify(mockRecruiterRepo, times(1)).findRecruiterByUsernameAndPassword(
                validRecruiter.getUsername(),
                validRecruiter.getPassword());
        assertEquals(new Principal(validRecruiter),actualResult);
    }

}
