package RotondaComidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import RotondaComidas.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
