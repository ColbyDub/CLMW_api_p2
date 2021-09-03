package com.revature.teamManager.data.repos;

import com.revature.teamManager.data.documents.Recruiter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruiterRepository extends MongoRepository<Recruiter,String> {

    Recruiter findRecruiterByUsernameAndPassword(String username, String password);
    Recruiter findRecruiterByUsername(String username);

}
