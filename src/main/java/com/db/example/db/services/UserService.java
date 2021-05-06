package com.db.example.db.services;

import com.db.example.db.entities.People;
import com.db.example.db.entities.Role;
import com.db.example.db.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.HashSet;

import java.util.Set;

@Service
public class UserService {
    private EntityManager entityManager;

    private CriteriaFactory criteriaFactory;

    private RolesService rolesService;

    private BCryptPasswordEncoder passwordEncoder;
    public UserService(EntityManager entityManager,
                       CriteriaFactory criteriaFactory,
                       RolesService rolesService,
                       BCryptPasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.criteriaFactory = criteriaFactory;
        this.rolesService = rolesService;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Transactional
    public void  save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(rolesService.findByName("USER"));
        user.setRoles(roles);
        if(user.getId() == null) {
            entityManager.persist(user);
        } else  {
            entityManager.merge(user);
        }
    }

    public User findByUserName(String userName) {
        CriteriaQuery<User> criteriaQuery = criteriaFactory.getCriteria(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        criteriaQuery.select(root).where(builder.equal(root.get("userName"), userName));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
