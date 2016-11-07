package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.ApiEntity;
import com.dbalthassat.restapi.exception.clientError.notFound.DataEmptyThenElementNotFoundException;
import com.dbalthassat.restapi.exception.clientError.notFound.IdNotFoundException;
import com.dbalthassat.restapi.utils.EntityMapper;
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
    public List<? extends ApiEntity> findAll(EntityMapper entityMapper) {
        Query query = em.createQuery("SELECT e FROM " + entityMapper.getEntityClass().getName() + " e");
        return query.getResultList();
    }

    public ApiEntity findFirst(EntityMapper entityMapper) {
        Query query = em.createQuery("SELECT e FROM " + entityMapper.getEntityClass().getName() + " e");
        query.setMaxResults(1);
        try {
            return (ApiEntity) query.getSingleResult();
        } catch(NoResultException e) {
            throw new DataEmptyThenElementNotFoundException(entityMapper.getResource());
        }
    }

    public ApiEntity findOne(EntityMapper entityMapper, Long id) {
        Query query = em.createQuery("SELECT e FROM " + entityMapper.getEntityClass().getName() + " e WHERE e.id = :id");
        query.setParameter("id", id);
        try {
            return (ApiEntity) query.getSingleResult();
        } catch(NoResultException e) {
            throw new IdNotFoundException(entityMapper.getResource(), id);
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
            Query count = em.createQuery("SELECT COUNT(p.id) FROM " + parent.getEntityClass().getName() + " p " +
                                         "WHERE p.id = :parentId");
            count.setParameter("parentId", parentId);
            Long result = (Long) count.getSingleResult();
            if(result == 0) {
                throw new IdNotFoundException(parent.getResource(), parentId);
            } else {
                throw new IdNotFoundException(resource.getResource(), id);
            }
        }
    }
}
