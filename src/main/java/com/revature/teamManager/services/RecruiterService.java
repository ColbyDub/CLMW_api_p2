package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.documents.Player;
import com.revature.teamManager.data.documents.Recruiter;
import com.revature.teamManager.data.repos.RecruiterRepository;
import com.revature.teamManager.util.PasswordUtils;
import com.revature.teamManager.util.exceptions.AuthenticationException;
import com.revature.teamManager.util.exceptions.InvalidRequestException;
import com.revature.teamManager.web.dtos.Principal;

public class RecruiterService {
    private RecruiterRepository recruiterRepository;
    private PasswordUtils passwordUtils;

    public RecruiterService(RecruiterRepository recruiterRepository, PasswordUtils passwordUtils){
        this.recruiterRepository = recruiterRepository;
        this.passwordUtils = passwordUtils;
    }

    public Principal login(String username, String password){

        String encryptedPassword = passwordUtils.generateSecurePassword(password);
        System.out.println("In login.. username "+username+"  pass "+password+"  encrypted pass "+encryptedPassword);
        Recruiter authRecruiter = recruiterRepository.findRecruiterByUsernameAndPassword(username,encryptedPassword);
        if(authRecruiter == null){
            throw new AuthenticationException("Invlaid credentials provided!");
        }
        return new Principal(authRecruiter);
    }

    public Recruiter register(Recruiter recruiter) {
        if (isValid(recruiter)) {
            return recruiterRepository.save(recruiter);
        }
        return null;
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
