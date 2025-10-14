package com.example.demo.Repository;

import com.example.demo.Entity.ToolCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolCatalogRepository extends JpaRepository<ToolCatalogEntity,Long> {

    List<ToolCatalogEntity> findByToolNameContainingIgnoreCase(String toolName);
}
