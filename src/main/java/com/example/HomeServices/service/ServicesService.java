package com.example.HomeServices.service;

import com.example.HomeServices.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ServicesService {

    private final ServiceRepository serviceRepository;

    public List<com.example.HomeServices.entity.Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public com.example.HomeServices.entity.Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Услуга с ID " + id + " не найдена"));
    }
}
