package com.db.example.db.repo;

import com.db.example.db.entities.People;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeopleRepo  extends JpaRepository<People, Integer> {
    People getById(Integer id);
    List<People> findAll();

}
