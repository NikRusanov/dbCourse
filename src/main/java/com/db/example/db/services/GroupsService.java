package com.db.example.db.services;


import com.db.example.db.entities.Group;
import com.db.example.db.entities.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class GroupsService {
    private EntityManager entityManager;

    private CriteriaFactory criteriaFactory;

    GroupsService(EntityManager entityManager, CriteriaFactory criteriaFactory) {
        this.entityManager = entityManager;
        this.criteriaFactory = criteriaFactory;
    }

    public List<Group> list() {
        CriteriaQuery<Group> criteriaQuery = criteriaFactory.getCriteria(Group.class);
        Root<Group> root = criteriaQuery.from(Group.class);
        criteriaQuery.select(root);
        return  entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Transactional
    public void  save(Group group) {
        if(group.getId() == null) {
            entityManager.persist(group);
        } else  {
            entityManager.merge(group);
        }

    }

    @Transactional
    public void delete(Group group) {
        Group mergedGroup = entityManager.merge(group);
        entityManager.remove(mergedGroup);
    }

    public Group findById(Integer id) {
        return entityManager.find(Group.class, id);
    }
}
