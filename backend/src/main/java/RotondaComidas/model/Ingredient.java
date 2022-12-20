package RotondaComidas.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "ingredient")
public class Ingredient implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ingredient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "ingredientOder_generator")
    private Long id;

    @Column(name ="quantity")
    private int quantity;

    @Column(name = "name",length = 50)
    private String name;
    
    @ManyToMany(cascade = {CascadeType.ALL},mappedBy = "ingredients")
    private Set<Food> foods;

}
