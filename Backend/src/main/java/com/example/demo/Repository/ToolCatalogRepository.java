package com.example.demo.Repository;

import com.example.demo.Entity.ToolCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolCatalogRepository extends JpaRepository<ToolCatalogEntity,Long> {
}
