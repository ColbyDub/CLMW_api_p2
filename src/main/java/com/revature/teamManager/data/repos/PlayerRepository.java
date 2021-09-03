package com.revature.teamManager.data.repos;

import com.revature.teamManager.data.documents.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    //@Query("{username: ?}") Custom queries using this annotation

    Player findPlayerByUsernameAndPassword(String username, String password);
    Player findPlayerByUsername(String username);

}
