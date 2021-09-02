package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.repos.CoachRepository;
import org.junit.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CoachServiceTestSuite {
    CoachService sut;

    private CoachRepository mockCoachRepo;

    @Before
    public void beforeEachTest() {
        mockCoachRepo = mock(CoachRepository.class);
        sut = new CoachService(mockCoachRepo);
    }

    @After
    public void afterEachTest() {
        sut = null;
    }

    @Test
    public void isValid_returnsTrue_whenGivenValidCourse() {
        // arrange
        Coach validCoach = new Coach();
        validCoach.setCoachName("Bob");
        validCoach.setUsername("Bobby");
        validCoach.setPassword("password");
        validCoach.setSport("Basketball");
        validCoach.setTeamName("Fighting TypeScripts");

        // act
        boolean actualResult = sut.isValid(validCoach);

        // assert
        Assert.assertTrue("Expected coach to be considered valid",actualResult);
    }

    @Test
    public void isValid_returnsFalse_whenGivenEmptyName() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act
        boolean actualResult = sut.isValid(invalidCoach);

        // assert
        Assert.assertFalse("Expected coach to be considered invalid",actualResult);
    }

    @Test
    public void isValid_returnsFalse_whenGivenEmptyUsername() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act
        boolean actualResult = sut.isValid(invalidCoach);

        // assert
        Assert.assertFalse("Expected coach to be considered invalid",actualResult);
    }

    @Test
    public void isValid_returnsFalse_whenGivenAlreadyExistingUsername() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("ExistingUsername");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");
        when(mockCoachRepo.findByUsername(any())).thenReturn(invalidCoach);

        // act
        boolean actualResult = sut.isValid(invalidCoach);

        // assert
        verify(mockCoachRepo, times(1)).findByUsername(invalidCoach.getUsername());
        Assert.assertFalse("Expected coach to be considered invalid",actualResult);
    }

    @Test
    public void isValid_returnsFalse_whenGivenEmptyPassword() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act
        boolean actualResult = sut.isValid(invalidCoach);

        // assert
        Assert.assertFalse("Expected coach to be considered invalid",actualResult);
    }

    @Test
    public void isValid_returnsFalse_whenGivenPasswordLessThan8Characters() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("pass");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act
        boolean actualResult = sut.isValid(invalidCoach);

        // assert
        Assert.assertFalse("Expected coach to be considered invalid",actualResult);
    }

    @Test
    public void isValid_returnsFalse_whenGivenEmptySport() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("");
        invalidCoach.setTeamName("Fighting TypeScripts");

        // act
        boolean actualResult = sut.isValid(invalidCoach);

        // assert
        Assert.assertFalse("Expected coach to be considered invalid",actualResult);
    }

    @Test
    public void isValid_returnsFalse_whenGivenEmptyTeamName() {
        // arrange
        Coach invalidCoach = new Coach();
        invalidCoach.setCoachName("Bob");
        invalidCoach.setUsername("Bobby");
        invalidCoach.setPassword("password");
        invalidCoach.setSport("Basketball");
        invalidCoach.setTeamName("");

        // act
        boolean actualResult = sut.isValid(invalidCoach);

        // assert
        Assert.assertFalse("Expected coach to be considered invalid",actualResult);
    }
}
