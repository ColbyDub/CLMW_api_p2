package com.revature.teamManager.data.repos;

import com.revature.teamManager.data.documents.Pin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends MongoRepository<Pin, String> {

    Pin findPinByType(String type);
    Pin findPinByEncryptedPin(String pin);

}
