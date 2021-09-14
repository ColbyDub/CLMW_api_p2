package com.revature.teamManager.data.repos;

import com.revature.teamManager.data.documents.Coach;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoachRepository extends MongoRepository<Coach,String> {

    Coach findCoachByUsernameAndPassword(String username, String password);
    Coach findCoachByUsername(String username);
    @Query("{'players': {$elemMatch: {$elemMatch: {$in: [?0]}}}}")
    Coach findCoachByPlayersContaining(String player);

}
