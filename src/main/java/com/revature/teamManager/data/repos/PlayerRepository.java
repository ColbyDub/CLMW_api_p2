package com.revature.teamManager.data.repos;

import com.revature.teamManager.data.documents.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    //@Query("{username: ?}") Custom queries using this annotation

    Player findPlayerByUsernameAndPassword(String username, String password);
    Player findPlayerByUsername(String username);
    @Query(value="{}",fields="{'username' : 1, 'name' : 1, 'teamName' : 1, 'invitations' : 1}")
    List <Player> findAll();


//    Player updatePlayerOffers(String username){
//        Player
//    }

}
