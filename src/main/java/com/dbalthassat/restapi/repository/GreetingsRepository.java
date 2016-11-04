package com.dbalthassat.restapi.repository;

import com.dbalthassat.restapi.entity.GreetingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GreetingsRepository extends JpaRepository<GreetingsEntity, Long> {
}
