package com.db.example.db.services;

import com.db.example.db.entities.Group;
import com.db.example.db.entities.Marks;
import com.db.example.db.entities.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class PeopleService {

    private EntityManager entityManager;

    private CriteriaFactory criteriaFactory;

    @Autowired
    PeopleService(EntityManager entityManager, CriteriaFactory criteriaFactory) {
        this.entityManager = entityManager;
        this.criteriaFactory = criteriaFactory;
    }

    public List<People> list() {
        CriteriaQuery<People> criteriaQuery = criteriaFactory.getCriteria(People.class);
        Root<People> root = criteriaQuery.from(People.class);
        criteriaQuery.select(root);
        return  entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Transactional
    public void  save(People people) {
        if(people.getId() == null) {
            entityManager.persist(people);
        } else  {
            entityManager.merge(people);
        }

    }

    @Transactional
    public void delete(People people) {
        People mergedPeople = entityManager.merge(people);
        entityManager.remove(mergedPeople);
    }

    public People findById(int id) {
        return entityManager.find(People.class, id);
    }

    public List<People> findByGroup(Group group) {
        CriteriaQuery<People> criteriaQuery = criteriaFactory.getCriteria(People.class);
        Root<People> root = criteriaQuery.from(People.class);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        Join<People, Group> joinGroup = root.join("group");
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(joinGroup.get("id"),group.getId()));
        criteriaQuery.select(root);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return  entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<People> findByType(char type) {
        CriteriaQuery<People> criteriaQuery = criteriaFactory.getCriteria(People.class);
        Root<People> root = criteriaQuery.from(People.class);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        Predicate onlyTeachers = builder.equal(root.get("type"), type);
        criteriaQuery.where(onlyTeachers);
        criteriaQuery.select(root);
        return  entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<Tuple> getLowerAverageMarksByTeacher(People people) {
        CriteriaQuery<Tuple> criteriaQuery = criteriaFactory.getCriteria(Tuple.class);
        Root<Marks> root = criteriaQuery.from(Marks.class);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        Join<Marks, People> teacherJoin = root.join("teacher");
        Join<Marks, People> studentJoin = root.join("student");
        Join<Marks, Group> groupJoin = studentJoin.join("group");

        Predicate teacherId = builder.equal(root.get("teacher"), people.getId());
        criteriaQuery.where(teacherId);
        Expression<Double> averageValues = builder.avg(root.get("value"));
        criteriaQuery.select(builder.tuple(groupJoin,averageValues));
        criteriaQuery.groupBy(studentJoin.get("group"), groupJoin.get("id"));
        criteriaQuery.orderBy(builder.asc(averageValues));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}