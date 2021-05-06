package com.db.example.db.services;

import com.db.example.db.entities.Group;
import com.db.example.db.entities.Mark;
import com.db.example.db.entities.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
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

    public List<People> findByName(String name) {
        CriteriaQuery<People> criteriaQuery = criteriaFactory.getCriteria(People.class);
        Root<People> root = criteriaQuery.from(People.class);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        String likePattern = name+"%";
        Predicate firstNamePred = builder.like(root.get("firstName"), likePattern);
        Predicate secondNamePred = builder.like(root.get("lastName"), name);
        criteriaQuery.select(root).where(builder.or(firstNamePred, secondNamePred));
        return entityManager.createQuery(criteriaQuery).getResultList();
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
        Root<Mark> root = criteriaQuery.from(Mark.class);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        Join<Mark, People> studentJoin = root.join("student");
        Join<Mark, Group> groupJoin = studentJoin.join("group");

        Predicate teacherId = builder.equal(root.get("teacher"), people.getId());
        criteriaQuery.where(teacherId);
        Expression<Double> averageValues = builder.avg(root.get("value"));
        criteriaQuery.select(builder.tuple(groupJoin,averageValues));
        criteriaQuery.groupBy(studentJoin.get("group"), groupJoin.get("id"));
        criteriaQuery.orderBy(builder.asc(averageValues));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}
