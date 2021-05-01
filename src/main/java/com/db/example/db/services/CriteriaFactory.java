package com.db.example.db.services;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;


@Service
public class CriteriaFactory {

    private EntityManager entityManager;

    public CriteriaFactory(EntityManager em) {
        this.entityManager = em;
    }


    public CriteriaQuery getCriteria(Class entityClass) {
        return entityManager.getCriteriaBuilder().createQuery(entityClass);
    }
}
