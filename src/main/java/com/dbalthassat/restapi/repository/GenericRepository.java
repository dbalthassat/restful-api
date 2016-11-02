package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.GenericEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Repository parent de tous les repository de l'application.
 *
 * Expose des méthodes génériques à ne pas réécrire pour chaque entité.
 *
 * @param <T> le type d'entité à lier au repository.
 */
@NoRepositoryBean
public interface GenericRepository<T extends GenericEntity> extends JpaRepository<T, Long> {
}
