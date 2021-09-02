package com.revature.teamManager.data.repos;

import java.util.List;

public interface CrudRepository<T> {

    List<T> findAll();
    T findById(String id);
    T save(T newResource);
    boolean update(T updatedResource);
    boolean deleteById(int id);

}
