package com.revature.teamManager.data.repos;

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

    public Object findByUsername(String id) {
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
