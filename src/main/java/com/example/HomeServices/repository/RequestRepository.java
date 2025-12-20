package com.example.HomeServices.repository;

import com.example.HomeServices.entity.Request;
import com.example.HomeServices.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByStatusIn(List<RequestStatus> statuses);

}