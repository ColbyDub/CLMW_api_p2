package com.revature.teamManager.data.repos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.revature.teamManager.data.documents.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepository implements CrudRepository{

    private final MongoCollection<AppUser> usersCollection;

    @Autowired
    public PlayerRepository(MongoClient mongoClient){
        this.usersCollection = mongoClient.getDatabase("project2").getCollection("users", AppUser.class);
    }

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public Object findById(String id) {
        return null;
    }

    @Override
    public Object save(Object newResource) {
        return null;
    }

    @Override
    public boolean update(Object updatedResource) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }

}
