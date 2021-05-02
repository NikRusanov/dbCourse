package com.db.example.db.repo;

import com.db.example.db.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepo extends CrudRepository<Group, Integer> {
}
