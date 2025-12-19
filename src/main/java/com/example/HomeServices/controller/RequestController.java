package com.example.HomeServices.controller;

import com.example.HomeServices.entity.Request;
import com.example.HomeServices.entity.RequestStatus;
import com.example.HomeServices.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestRepository requestRepository;

    @GetMapping("/current")
    public List<Request> getCurrentRequests() {
        return requestRepository.findByStatusIn(List.of(RequestStatus.PENDING, RequestStatus.ACCEPTED));
    }

    @GetMapping("/history")
    public List<Request> getHistoryRequests() {
        return requestRepository.findByStatusIn(List.of(RequestStatus.CANCELLED, RequestStatus.COMPLETED));
    }

    @PostMapping("/create")
    public Request createRequest(@RequestBody Request request) {
        return requestRepository.save(request);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Request> cancelRequest(@PathVariable Long id) {
        return requestRepository.findById(id)
                .map(req -> {
                    req.setStatus(RequestStatus.CANCELLED);
                    Request updated = requestRepository.save(req);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
