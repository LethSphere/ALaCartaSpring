package RotondaComidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import RotondaComidas.model.Food;


public interface FoodRepository extends JpaRepository<Food, Long> {
}
