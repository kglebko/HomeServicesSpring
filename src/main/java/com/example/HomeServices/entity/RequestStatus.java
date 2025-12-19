package com.example.HomeServices.entity;

public enum RequestStatus {
    // Текущие
    PENDING,    // На рассмотрении
    ACCEPTED,   // Принята

    // Выполненные
    CANCELLED,  // Отменена
    COMPLETED   // Выполнена
}
