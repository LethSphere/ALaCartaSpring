package com.inn.rotonda.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.inn.rotonda.POJO.Restaurant;
import com.inn.rotonda.constents.RotondaConstants;
import com.inn.rotonda.dao.RestaurantDao;
import com.inn.rotonda.service.RestaurantService;
import com.inn.rotonda.utils.RotondaUtils;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired 
    RestaurantDao restaurantDao;

    @Override
    public ResponseEntity<String> addNewRestaurant(Map<String, String> requestMap) {
        try {
            if(validateRestaurantMap(requestMap, false)){
                restaurantDao.save(getRestaurantFromMap(requestMap, false));
                return RotondaUtils.getResponseEntity("Restaurante añadido",HttpStatus.OK)
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RotondaUtils.getResponseEntity(RotondaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateRestaurantMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Restaurant getRestaurantFromMap(Map<String,String> requestMap, boolean isAdd){
        Restaurant restaurant= new Restaurant();
        if(isAdd){
            restaurant.setId(Integer.parseInt(requestMap.get("id")));
        }
        restaurant.setName(requestMap.get("name"));
        restaurant.setDireccion(requestMap.get("direccion"));
        restaurant.setDescripcion(requestMap.get("descripcion"));
        return restaurant;
    }
    
}
