package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.exception.clientError.notFound.IdNotFoundException;
import com.dbalthassat.restapi.utils.EntityMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.StringJoiner;

@Repository
public class GenericRepository {
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<? extends ApiEntity> findAll(EntityMapper entityMapper) {
        Query query = em.createQuery("SELECT e FROM " + entityMapper.getEntityClass().getName() + " e");
        return query.getResultList();
    }

    public ApiEntity findOne(EntityMapper entityMapper, Long id) {
        Query query = em.createQuery("SELECT e FROM " + entityMapper.getEntityClass().getName() + " e WHERE e.id = :id");
        query.setParameter("id", id);
        try {
            return (ApiEntity) query.getSingleResult();
        } catch(NoResultException e) {
            throw new IdNotFoundException("/" + entityMapper.getResource() + "/" + id);
        }
    }

    @SuppressWarnings("unchecked")
    public List<? extends ApiEntity> findAllWithParent(EntityMapper parent, Long parentId, EntityMapper resource) {
        Query query = em.createQuery("SELECT e FROM " + parent.getEntityClass().getName() + " p " +
                                     "JOIN p." + resource.getResource() + " e WHERE p.id = :parentId");
        query.setParameter("parentId", parentId);
        return query.getResultList();
    }

    public ApiEntity findOneWithParent(EntityMapper parent, Long parentId, EntityMapper resource, Long id) {
        Query query = em.createQuery("SELECT e FROM " + parent.getEntityClass().getName() + " p " +
                "JOIN p." + resource.getResource() + " e WHERE p.id = :parentId AND e.id = :id");
        query.setParameter("parentId", parentId);
        query.setParameter("id", id);
        try {
            return (ApiEntity) query.getSingleResult();
        } catch(NoResultException e) {
            StringJoiner errorMessage = new StringJoiner("/");
            errorMessage.add("").add(parent.getResource()).add(Long.toString(parentId)).add(resource.getResource()).add(Long.toString(id));
            throw new IdNotFoundException(errorMessage.toString());
        }
    }
}
