package com.revature.teamManager.data.repos;

import com.revature.teamManager.data.documents.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.List;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    //@Query("{username: ?}") Custom queries using this annotation

    Player findPlayerByUsernameAndPassword(String username, String password);
    Player findPlayerByUsername(String username);

    @Query(value="{}",fields="{'username' : 1, 'name' : 1, 'sport' : 1, 'teamName' : 1, 'skills' : 1, 'invitations' : 1}")
    List <Player> findAll();
    @Query("{'sports': {$elemMatch: {$in: [?0]}}}")
    List <Player> findPlayersBySport(String sport);

//    Player updatePlayerOffers(String username){
//        Player
//    }

}
