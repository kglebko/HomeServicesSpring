package com.example.HomeServices.controller;

import com.example.HomeServices.entity.Service;
import com.example.HomeServices.service.ServicesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Услуги", description = "API для управления услугами")
public class ServiceController {

    private final ServicesService servicesService;

    @GetMapping("/latest")
    @Operation(summary = "Получить список доступных услуг")
    public ResponseEntity<List<Service>> getLatestServices() {
        List<Service> services = servicesService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping
    @Operation(summary = "Получить все услуги")
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = servicesService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить услугу по ID")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        Service service = servicesService.getServiceById(id);
        return ResponseEntity.ok(service);
    }
}