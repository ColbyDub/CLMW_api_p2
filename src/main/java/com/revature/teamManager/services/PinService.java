package com.revature.teamManager.services;

import com.revature.teamManager.data.documents.Coach;
import com.revature.teamManager.data.documents.Pin;
import com.revature.teamManager.data.repos.PinRepository;
import com.revature.teamManager.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PinService {

    private PasswordUtils passwordUtils;
    private PinRepository pinRepository;

    @Autowired
    public PinService(PinRepository pinRepository, PasswordUtils passwordUtils){
        this.passwordUtils = passwordUtils;
        this.pinRepository = pinRepository;
    }

    public Pin register(Pin pin) {
        pin.setEncryptedPin(passwordUtils.generateSecurePin(pin.getEncryptedPin()));
        return pinRepository.save(pin);
    }

    public Pin checkPin(String encryptedPin){
        System.out.println("CHECKING: "+encryptedPin);
        return pinRepository.findPinByEncryptedPin(encryptedPin);
    }


}
