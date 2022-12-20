package RotondaComidas.service.services;

import org.springframework.http.ResponseEntity;

import RotondaComidas.model.Ingredient;
import RotondaComidas.model.DTOs.IngredientAddRequest;

public interface IngredientService {
    Ingredient findIngredientById(Long ingredient_id);
    Ingredient findIngredientByName(String name);
    int findNumberOfIngredient();
    Ingredient createIngredient(String Name, int quantity);
    void deleteIngredient(Long ingredient_id);
    ResponseEntity<?> registryIngredient(IngredientAddRequest ingredientAddRequest);

}
