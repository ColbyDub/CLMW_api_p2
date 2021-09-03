package com.revature.teamManager.data.repos;

import com.revature.teamManager.data.documents.Coach;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepository extends MongoRepository<Coach,String> {

    Coach findCoachByUsernameAndPassword(String username, String password);
    Coach findCoachByUsername(String username);

}
