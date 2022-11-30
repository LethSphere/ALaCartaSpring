package com.inn.rotonda.POJO;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@NamedQuery(name="Restaurant.getAllRestaurant", query = "select r from Restaurant r")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="Restaurante")

public class Restaurant implements Serializable{

    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Restaurante_ID")
    private Integer id;

    @Column(name="Restaurante_Nombre")
    private String name;

    @Column(name="Direccion")
    private String direccion;

    @Column(name="Descripcion")
    private String descripcion;
}
