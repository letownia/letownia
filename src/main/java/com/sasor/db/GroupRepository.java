package com.sasor.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepository extends CrudRepository<GroupObject,String>{
    @Query("select u from GroupObject u where u.groupName = ?1")
    Optional<GroupObject> getGroupByName(String groupName);
}
