package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.exception.clientError.notFound.IdNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class GenericRepository {
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Object> findAll(String entityName) {
        Query query = em.createQuery("SELECT e FROM " + entityName + "Entity e");
        return query.getResultList();
    }

    public Object findOne(String entityName, Long id) {
        Query query = em.createQuery("SELECT e FROM " + entityName + "Entity e WHERE e.id = :id");
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch(NoResultException e) {
            throw new IdNotFoundException(id);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Object> findAllWithParent(String parent, Long parentId, String resource) {
        Query query = em.createQuery("SELECT e FROM " + parent + "Entity p JOIN p." + resource.toLowerCase() + " e WHERE p.id = :parentId");
        query.setParameter("parentId", parentId);
        return query.getResultList();
    }
}
