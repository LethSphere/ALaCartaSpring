package com.inn.rotonda.serviceImpl;

import java.util.Map;
import java.util.Objects;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.rotonda.POJO.User;
import com.inn.rotonda.constents.RotondaConstants;
import com.inn.rotonda.dao.UserDao;
import com.inn.rotonda.service.UserService;
import com.inn.rotonda.utils.RotondaUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> signUp(Map<String,String> requestMap) {
        log.info("Inside signup {}",requestMap);
        try {
            if(validateSignUpMap(requestMap)){
                User user = userDao.findByName(requestMap.get("name"));
                if(Objects.isNull(user)){
                    userDao.save(getUserFromMap(requestMap));
                    return RotondaUtils.getResponseEntity("Funciona wii", HttpStatus.OK);
                }else{
                    return RotondaUtils.getResponseEntity("Ya existe el usuario",HttpStatus.BAD_REQUEST);
                }
            }else{
                return RotondaUtils.getResponseEntity(RotondaConstants.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
            } 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return RotondaUtils.getResponseEntity(RotondaConstants.SOMETHING_WENT_WRONG,HttpStatus.BAD_REQUEST);
    }
    
    private boolean validateSignUpMap(java.util.Map<String,String> requestMap){

        if(requestMap.containsKey("name") && requestMap.containsKey("idDireccion")){
            return true;
        } else {
            return false;
        }
        
    }

    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setIdDireccion(requestMap.get("idDireccion"));
        user.setName(requestMap.get("name"));
        return user;
    }
}
