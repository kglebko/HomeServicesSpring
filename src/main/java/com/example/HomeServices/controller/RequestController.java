package com.example.HomeServices.controller;

import com.example.HomeServices.dto.CreateRequestDto;
import com.example.HomeServices.dto.RequestDTO;
import com.example.HomeServices.entity.Request;
import com.example.HomeServices.entity.RequestStatus;
import com.example.HomeServices.entity.Service;
import com.example.HomeServices.repository.RequestRepository;
import com.example.HomeServices.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestRepository requestRepository;
    private final ServiceRepository serviceRepository;

    @PostMapping
    public ResponseEntity<Request> createRequest(@RequestBody CreateRequestDto dto) {
        Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + dto.getServiceId()));

        Request request = new Request();
        request.setService(service);
        request.setStatus(RequestStatus.PENDING); // Устанавливаем статус PENDING
        request.setSelectedDate(dto.getSelectedDate());
        request.setSelectedStartTime(dto.getSelectedStartTime());
        request.setSelectedEndTime(dto.getSelectedEndTime());
        request.setComment(dto.getComment());
        request.setEstimatedPrice(dto.getEstimatedPrice());
        request.setUserId(dto.getUserId());

        Request savedRequest = requestRepository.save(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/current")
    public List<RequestDTO> getCurrentRequests() {
        return requestRepository.findByStatusIn(List.of(RequestStatus.PENDING, RequestStatus.ACCEPTED))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/history")
    public List<RequestDTO> getHistoryRequests() {
        return requestRepository.findByStatusIn(List.of(RequestStatus.CANCELLED, RequestStatus.COMPLETED))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<RequestDTO> cancelRequest(@PathVariable Long id) {
        return requestRepository.findById(id)
                .map(req -> {
                    req.setStatus(RequestStatus.CANCELLED);
                    Request updated = requestRepository.save(req);
                    return ResponseEntity.ok(convertToDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private RequestDTO convertToDTO(Request req) {
        return new RequestDTO(
                req.getId(),
                req.getService().getName(),
                req.getStatus().name(),
                req.getSelectedDate() + " " +
                        (req.getSelectedStartTime() != null ? req.getSelectedStartTime() : ""),
                req.getActualTime() != null ? req.getActualTime().toString() : null,
                req.getEstimatedPrice() != null ? req.getEstimatedPrice() : req.getService().getPrice(),
                req.getActualPrice(),
                req.getComment()
        );
    }
}
