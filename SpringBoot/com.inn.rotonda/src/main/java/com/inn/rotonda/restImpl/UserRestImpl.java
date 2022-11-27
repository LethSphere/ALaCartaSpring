package com.inn.rotonda.restImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.rotonda.constents.RotondaConstants;
import com.inn.rotonda.rest.UserRest;
import com.inn.rotonda.service.UserService;
import com.inn.rotonda.utils.RotondaUtils;

@RestController
public class UserRestImpl  implements UserRest{

    @Autowired
    UserService userService;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
       
        try {
            return userService.signUp(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RotondaUtils.getResponseEntity(RotondaConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    
}
