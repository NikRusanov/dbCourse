package com.db.example.db.services;

import com.db.example.db.entities.security.Role;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

@Service
public class RolesService {
        private EntityManager entityManager;

        private CriteriaFactory criteriaFactory;


        public RolesService(EntityManager entityManager, CriteriaFactory criteriaFactory) {
            this.entityManager = entityManager;
            this.criteriaFactory = criteriaFactory;
        }

        public Role findById(Long id) {
            return entityManager.find(Role.class, id);
        }

        @Transactional
        public void  save(Role role) {
            if(role.getId() == null) {
                entityManager.persist(role);
            } else  {
                entityManager.merge(role);
            }
        }

    public Role findByName(String role) {
        CriteriaQuery<Role> criteriaQuery = criteriaFactory.getCriteria(Role.class);
        Root<Role> root = criteriaQuery.from(Role.class);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        criteriaQuery.select(root).where(builder.equal(root.get("name"), role));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
