package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.GreetingsEntity;

public interface GreetingsRepository extends GenericRepository<GreetingsEntity> {
    // Fonctionne bien :
    //@Query("SELECT e FROM GreetingsEntity e WHERE e.id > :id")
    //Page<GreetingsEntity> findByIdGreaterThan(@Param("id") long id, Pageable pageable);
}
