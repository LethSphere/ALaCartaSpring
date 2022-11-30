package com.inn.rotonda.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.inn.rotonda.constents.RotondaConstants;
import com.inn.rotonda.rest.RestaurantRest;
import com.inn.rotonda.service.RestaurantService;
import com.inn.rotonda.utils.RotondaUtils;

@RestController
public class RestaurantRestImpl implements RestaurantRest{

    @Autowired
    RestaurantService restaurantService;

    @Override
    public ResponseEntity<String> addNewRestaurant(Map<String, String> requestMap) {
        try {
            return restaurantService.addNewRestaurant(requestMap);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RotondaUtils.getResponseEntity(RotondaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
