package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.documents.Pin;
import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.documents.Recruiter;
import com.revature.teamManager.data.repos.PinRepository;
import com.revature.teamManager.data.repos.RecruiterRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.web.dtos.Principal;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {
    private RecruiterRepository recruiterRepository;
    private PinRepository pinRepository;
    private PasswordUtils passwordUtils;

    public RecruiterService(RecruiterRepository recruiterRepository, PinRepository pinRepository, PasswordUtils passwordUtils){
        this.recruiterRepository = recruiterRepository;
        this.pinRepository = pinRepository;
        this.passwordUtils = passwordUtils;
    }

    public Principal login(String username, String password){
        String encryptedPassword = passwordUtils.generateSecurePassword(password);
        Recruiter authRecruiter = recruiterRepository.findRecruiterByUsernameAndPassword(username,encryptedPassword);

        if(authRecruiter == null){
            throw new AuthenticationException("Invalid credentials provided!");
        }
        return new Principal(authRecruiter);
    }

    public Recruiter register(Recruiter recruiter, String pin) {
        System.out.println(pin);
        String encryptedPin = passwordUtils.generateSecurePin(pin);
        System.out.println(encryptedPin);
        Pin checkPin = pinRepository.findPinByEncryptedPin(encryptedPin);
        System.out.println(checkPin);

        if(checkPin == null || !checkPin.getType().equals("recruiter")){
            throw new AuthenticationException("Invalid Pin");
        }

        if (!isValid(recruiter)) {
            return null;
        }

        String encryptedPass = passwordUtils.generateSecurePassword(recruiter.getPassword());

        return recruiterRepository.save(new Recruiter(recruiter.getName(), recruiter.getUsername(), encryptedPass));
    }

    public boolean isValid(Recruiter recruiter) {
        if (recruiter.getName() == null || recruiter.getName().trim().equals("")){
            throw new InvalidRequestException("Name cannot be left blank");
        }

        if(recruiter.getUsername() == null || recruiter.getUsername().trim().equals("")){
            throw new InvalidRequestException("Username cannot be left blank");
        }

        if (recruiter.getPassword().length() < 8) {
            throw new InvalidRequestException("Your password must be at least 8 characters long");
        }
        if (recruiterRepository.findRecruiterByUsername(recruiter.getUsername()) != null) {
            throw new InvalidRequestException("That username is already taken");
        }

        return true;
    }


}
