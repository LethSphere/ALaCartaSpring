package com.inn.rotonda.rest;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/restaurant")
public interface RestaurantRest {
    
    @PostMapping(path = "/add")
    ResponseEntity<String> addNewRestaurant(@RequestBody(required = true)Map<String,String> requestMap);

}
