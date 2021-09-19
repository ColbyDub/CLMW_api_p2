package com.revature.teamManager.data.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pins")
public class Pin {
    private String encryptedPin;
    private String type;

    public Pin(){}

    public Pin(String type, String encryptedPin) {
        this.type = type;
        this.encryptedPin = encryptedPin;
    }

    public String getEncryptedPin() {
        return encryptedPin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEncryptedPin(String encryptedPin) {
        this.encryptedPin = encryptedPin;
    }
}
