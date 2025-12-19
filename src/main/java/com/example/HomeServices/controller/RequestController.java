package com.example.HomeServices.controller;

import com.example.HomeServices.dto.RequestDTO;
import com.example.HomeServices.entity.Request;
import com.example.HomeServices.entity.RequestStatus;
import com.example.HomeServices.repository.RequestRepository;
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

    @GetMapping("/current")
    public List<RequestDTO> getCurrentRequests() {
        return requestRepository.findByStatusIn(List.of(RequestStatus.PENDING, RequestStatus.ACCEPTED))
                .stream()
                .map(req -> new RequestDTO(
                        req.getId(),
                        req.getService().getName(),
                        req.getStatus().name(),
                        req.getSelectedDate() + " " +
                                (req.getSelectedStartTime() != null ? req.getSelectedStartTime() : ""),
                        req.getActualTime() != null ? req.getActualTime().toString() : null,
                        req.getEstimatedPrice() != null ? req.getEstimatedPrice() : req.getService().getPrice(),
                        req.getActualPrice(),
                        req.getComment()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/history")
    public List<RequestDTO> getHistoryRequests() {
        return requestRepository.findByStatusIn(List.of(RequestStatus.CANCELLED, RequestStatus.COMPLETED))
                .stream()
                .map(req -> new RequestDTO(
                        req.getId(),
                        req.getService().getName(),
                        req.getStatus().name(),
                        req.getSelectedDate() + " " +
                                (req.getSelectedStartTime() != null ? req.getSelectedStartTime() : ""),
                        req.getActualTime() != null ? req.getActualTime().toString() : null,
                        req.getEstimatedPrice() != null ? req.getEstimatedPrice() : req.getService().getPrice(),
                        req.getActualPrice(),
                        req.getComment()
                ))
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    public Request createRequest(@RequestBody Request request) {
        return requestRepository.save(request);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<RequestDTO> cancelRequest(@PathVariable Long id) {
        return requestRepository.findById(id)
                .map(req -> {
                    req.setStatus(RequestStatus.CANCELLED);
                    Request updated = requestRepository.save(req);
                    RequestDTO dto = new RequestDTO(
                            updated.getId(),
                            updated.getService().getName(),
                            updated.getStatus().name(),
                            updated.getSelectedDate() + " " +
                                    (updated.getSelectedStartTime() != null ? updated.getSelectedStartTime() : ""),
                            updated.getActualTime() != null ? updated.getActualTime().toString() : null,
                            updated.getEstimatedPrice() != null ? updated.getEstimatedPrice() : updated.getService().getPrice(),
                            updated.getActualPrice(),
                            updated.getComment()
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
