package foodPanda.service.services;

import org.springframework.http.ResponseEntity;

import foodPanda.model.Ingredient;
import foodPanda.model.DTOs.IngredientAddRequest;

public interface IngredientService {
    Ingredient findIngredientById(Long ingredient_id);
    Ingredient findIngredientByName(String name);
    int findNumberOfIngredient();
    Ingredient createIngredient(String Name, int quantity);
    void deleteIngredient(Long ingredient_id);
    ResponseEntity<?> registryIngredient(IngredientAddRequest ingredientAddRequest);

}
