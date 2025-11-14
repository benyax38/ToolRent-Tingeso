package com.example.demo.Repository;

import com.example.demo.Entity.PenaltyConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PenaltyConfigRepository extends JpaRepository<PenaltyConfigEntity,Long> {

    Optional<PenaltyConfigEntity> findTopByOrderByPenaltyConfigIdDesc();
}
