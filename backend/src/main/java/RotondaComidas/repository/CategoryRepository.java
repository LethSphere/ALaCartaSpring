package RotondaComidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import RotondaComidas.model.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}
