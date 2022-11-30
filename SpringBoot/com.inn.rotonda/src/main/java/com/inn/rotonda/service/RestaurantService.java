package com.inn.rotonda.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface RestaurantService {
    
    ResponseEntity<String> addNewRestaurant(Map<String, String> requestMap);
}
