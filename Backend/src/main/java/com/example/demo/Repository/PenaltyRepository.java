package com.example.demo.Repository;

import com.example.demo.Entity.PenaltyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyRepository extends JpaRepository<PenaltyEntity,Long> {
}
