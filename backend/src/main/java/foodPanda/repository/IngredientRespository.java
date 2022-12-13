package foodPanda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import foodPanda.model.Ingredient;

import java.util.Optional;

@Repository
public interface IngredientRespository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);
    Optional<Ingredient> findByid(Long id);
}
