package com.revature.teamManager.data.repos;

import com.revature.teamManager.data.documents.Coach;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CoachRepository implements CrudRepository{

    @Override
    public List findAll() {
        return null;
    }

    @Override
    public Object findById(String id) {
        return null;
    }

    public Coach findByUsername(String id) {
        return null;
    }

    @Override
    public Coach save(Object newResource) {
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
