package com.db.example.db.services;

import com.db.example.db.entities.Mark;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class MarksService {
    private final EntityManager entityManager;

    private final CriteriaFactory criteriaFactory;

    public MarksService (EntityManager entityManager, CriteriaFactory criteriaFactory){
        this.entityManager = entityManager;
        this.criteriaFactory = criteriaFactory;
    }
    public List<Mark> list() {
        CriteriaQuery<Mark> criteriaQuery = criteriaFactory.getCriteria(Mark.class);
        Root<Mark> root = criteriaQuery.from(Mark.class);
        criteriaQuery.select(root);
        return  entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Transactional
    public void  save(Mark mark) {
        if(mark.getId() == 0) {
            entityManager.persist(mark);
        } else  {
            entityManager.merge(mark);
        }

    }

    @Transactional
    public void delete(Mark mark) {
        Mark mergedMark = entityManager.merge(mark);
        entityManager.remove(mergedMark);
    }

    public Mark findById(int id) {
        return entityManager.find(Mark.class, id);
    }
}
