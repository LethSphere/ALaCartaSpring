package com.inn.rotonda.serviceImpl;

import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.rotonda.constents.RotondaConstants;
import com.inn.rotonda.service.UserService;
import com.inn.rotonda.utils.RotondaUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Override
    public ResponseEntity<String> signUp(Map<String,String> requestMap) {
        log.info("Inside signup {}",requestMap);
        if(validateSignUpMap(requestMap)){
            
        }else{
            return RotondaUtils.getResponseEntity(RotondaConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
    
    private boolean validateSignUpMap(java.util.Map<String,String> requestMap){

        if(requestMap.containsKey("Usuario_Nombre") && requestMap.containsKey("Usuario_Direccion")){
            return true;
        } else {
            return false;
        }
        
     }
}
