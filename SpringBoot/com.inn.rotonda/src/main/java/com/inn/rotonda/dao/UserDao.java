package com.inn.rotonda.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.inn.rotonda.POJO.User;

public interface UserDao extends JpaRepository<User, Integer>{
    
    User findByName(@Param("name") String name);
}
