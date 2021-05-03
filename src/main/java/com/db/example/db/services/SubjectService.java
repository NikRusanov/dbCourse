package com.db.example.db.services;


import com.db.example.db.entities.Subject;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;


@Component
public class SubjectService {
    private EntityManager entityManager;

    private CriteriaFactory criteriaFactory;

    SubjectService(EntityManager entityManager, CriteriaFactory criteriaFactory) {
        this.entityManager = entityManager;
        this.criteriaFactory = criteriaFactory;
    }

    public List<Subject> list() {
        CriteriaQuery<Subject> criteriaQuery = criteriaFactory.getCriteria(Subject.class);
        Root<Subject> root = criteriaQuery.from(Subject.class);
        criteriaQuery.select(root);
        return  entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Transactional
    public void save(Subject subject) {
        if(subject.getId() == 0) {
            entityManager.persist(subject);
        } else  {
            entityManager.merge(subject);
        }
    }

    @Transactional
    public void delete(Subject subject) {
        Subject merged = entityManager.merge(subject);
        entityManager.remove(merged);
    }

    public Subject findById(Integer id) {
        return entityManager.find(Subject.class, id);
    }
}
