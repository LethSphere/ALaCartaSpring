package RotondaComidas.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import RotondaComidas.model.Ingredient;
import RotondaComidas.model.DTOs.IngredientAddRequest;
import RotondaComidas.repository.IngredientRespository;
import RotondaComidas.service.services.IngredientService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Service
public class IngredientImplement implements IngredientService{

    @Autowired
	private IngredientRespository ingredientRespository;

    @Override
    public Ingredient findIngredientById(Long ingredient_id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Ingredient findIngredientByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int findNumberOfIngredient() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Ingredient createIngredient(String name, int quantity) {
        Ingredient ingrediente = new Ingredient();
        ingrediente.setName(name);
        ingrediente.setQuantity(quantity);
        log.info("Save user to repository with name: {}, quantity: {}", name, quantity);
        return ingredientRespository.save(ingrediente);
    }

    @Override
    public ResponseEntity<?> registryIngredient(IngredientAddRequest ingredientAddRequest) {
        Ingredient ingrediente = createIngredient(ingredientAddRequest.getName(),ingredientAddRequest.getQuantity());
        log.info("In registerIngredient IngredientImplement - register ingredient: '{}'", ingrediente.getName());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Override
    public void deleteIngredient(Long ingredient_id) {
        // TODO Auto-generated method stub
        
    }
    
}
