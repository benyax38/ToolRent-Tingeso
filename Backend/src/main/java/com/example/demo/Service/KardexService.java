package com.example.demo.Service;

import com.example.demo.Entity.KardexEntity;
import com.example.demo.Repository.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KardexService {

    private final KardexRepository kardexRepository;

    @Autowired
    public KardexService(KardexRepository kardexRepository) { this.kardexRepository = kardexRepository; }

    public List<KardexEntity> getAllKardexes() {
        return kardexRepository.findAll();
    }

    public KardexEntity createKardexes(KardexEntity kardex) {
        return kardexRepository.save(kardex);
    }

    public void deleteKardexesById(Long id) {
        if (!kardexRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe kardex con id: " + id);
        }
        kardexRepository.deleteById(id);
    }
}
