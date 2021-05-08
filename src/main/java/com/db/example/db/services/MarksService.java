package com.db.example.db.services;

import com.db.example.db.entities.Mark;
import com.db.example.db.entities.People;
import com.db.example.db.entities.Subject;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    public List<Mark> findFilter(String teacherName, String studentName, String subjectName) {
        CriteriaQuery<Mark> criteriaQuery = criteriaFactory.getCriteria(Mark.class);
        Root<Mark> root = criteriaQuery.from(Mark.class);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        List<Predicate> patterns = new ArrayList<>();
        Join<Mark, People> teacherJoin = root.join("teacher");
        Join<Mark, People> studentJoin = root.join("student");
        Join<Mark, Subject> subjectJoin = root.join("subject");

        if (teacherName != null && !teacherName.isEmpty()) {
            String teacherPattern = likePattern(teacherName);
            patterns.add(builder.like(teacherJoin.get("firstName"), teacherPattern));
        }
        if (studentName != null && !studentName.isEmpty()) {
            String studentPattern = likePattern(studentName);
            patterns.add(builder.like(studentJoin.get("firstName"), studentPattern));
        }
        if (subjectName != null && !subjectName.isEmpty()) {
            String subjectPattern = likePattern(subjectName);
            patterns.add(builder.like(subjectJoin.get("name"), subjectPattern));
        }

        criteriaQuery.select(root).where(builder.and(patterns.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private String likePattern(String str) {
        String result = "";
        if(str != null) {
            result = str + "%";
        } else {
            result = "%";
        }
        return result;
    }
}
