package RotondaComidas.model.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientAddRequest {
    
    private String name;

    private int quantity; 
}
