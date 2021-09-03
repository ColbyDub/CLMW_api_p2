package com.revature.teamManager.data.repos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.revature.teamManager.data.documents.AppUser;
import com.revature.teamManager.data.documents.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    //@Query("{username: ?}") Custom queries using this annotation

    Player findPlayerByUsernameAndPassword(String username, String password);
    Player findPlayerByUsername(String username);

}
