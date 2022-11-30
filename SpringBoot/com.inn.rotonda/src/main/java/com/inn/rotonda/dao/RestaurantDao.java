package com.inn.rotonda.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.rotonda.POJO.Restaurant;

import java.util.List;

public interface RestaurantDao extends JpaRepository<Restaurant, Integer>{
    
    List<Restaurant> getAllRestaurant();

}
