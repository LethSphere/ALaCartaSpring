package com.inn.rotonda.POJO;

import java.io.Serializable;

import javax.annotation.Generated;
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

@NamedQuery(name= "User.findByNombreId", query = "select u from User u where u.name=u.name")

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name="Usuario")

public class User implements Serializable{
    
    private static final long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Usuario_ID")
    private Integer id;

    @Column(name = "Usuario_Nombre")
    private String name;

    @Column(name = "Usuario_Direccion")
    private String idDireccion;


    
}
