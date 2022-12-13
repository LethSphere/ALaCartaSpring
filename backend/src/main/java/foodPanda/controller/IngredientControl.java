package foodPanda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import foodPanda.model.DTOs.IngredientAddRequest;
import foodPanda.service.services.IngredientService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("ingredient")
public class IngredientControl {

    @Autowired
    private IngredientService IngredientService;


	@PostMapping("add")
    public ResponseEntity<?> registerIngredient(@RequestBody IngredientAddRequest ingredientAddRequest){
        log.info("request: {}", ingredientAddRequest);
        return IngredientService.registryIngredient(ingredientAddRequest);
    }
}
