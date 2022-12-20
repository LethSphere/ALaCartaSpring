package RotondaComidas.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import RotondaComidas.exception.InvalidCredentialsException;
import RotondaComidas.exception.InvalidInputException;
import RotondaComidas.model.*;
import RotondaComidas.model.DTOs.AccountDTO;
import RotondaComidas.service.impl.AdministratorServiceImpl;
import RotondaComidas.service.impl.RestaurantServiceImpl;
import RotondaComidas.service.impl.UserServiceImpl;
import RotondaComidas.service.impl.ZoneServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("admin")
public class AdministratorController {

    private final Logger LOGGER = LogManager.getLogger(AdministratorController.class);

    @Autowired
    private AdministratorServiceImpl administratorServiceImpl;

    @Autowired
    private ZoneServiceImpl zoneServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private RestaurantServiceImpl restaurantServiceImpl;

    @PostMapping("register")
    public ResponseEntity<Administrator> register(@RequestBody(required = false) AccountDTO accountDTO) throws InvalidInputException {
        LOGGER.info("New register request with email=" + accountDTO.getCredential());
        return new ResponseEntity<>(administratorServiceImpl.saveAdministrator(accountDTO), HttpStatus.CREATED);
    }

 
    @PostMapping("auth")
    public ResponseEntity<Administrator> authenticate(@RequestBody AccountDTO accountDTO) throws InvalidCredentialsException {
        LOGGER.info("New authentication request with email=" + accountDTO.getCredential());
        return new ResponseEntity<>(administratorServiceImpl.authenticate(accountDTO), HttpStatus.OK);
    }


    @PostMapping("addRestaurant")
    public ResponseEntity<Restaurant> addRestaurant(@RequestParam(name = "id") long adminId, @RequestBody Restaurant restaurant) throws InvalidInputException {
        LOGGER.info("New addRestaurant request for adminId=" + adminId);
        return new ResponseEntity<>(administratorServiceImpl.addRestaurant(adminId, restaurant), HttpStatus.CREATED);
    }


    @PostMapping("addFood")
    public ResponseEntity<Food> addFood(@RequestParam(name = "categoryId", required = false) Long categoryId, @RequestBody(required = false) Food food) {
        LOGGER.info("New addFood request with categoryId=" + categoryId);
        return new ResponseEntity<>(administratorServiceImpl.addFoodForCategory(categoryId, food), HttpStatus.CREATED);
    }

 
    @GetMapping("fetchMenu")
    public ResponseEntity<Menu> fetchMenu(@RequestParam(name = "restaurantId", required = false) Long restaurantId) throws RuntimeException {
        LOGGER.info("New fetchMenu request with restaurantId=" + restaurantId);
        return new ResponseEntity<>(restaurantServiceImpl.fetchMenu(restaurantId), HttpStatus.OK);
    }

 
    @GetMapping("fetchZones")
    public ResponseEntity<APIResponse<Zone>> fetchZones() {
        return new ResponseEntity<>(APIResponse.<Zone>builder().response(zoneServiceImpl.fetchAll()).build(), HttpStatus.OK);
    }


    @PostMapping("changeStatus")
    public ResponseEntity<PandaOrder> changeOrderStatus(@RequestParam(name = "orderId", required = false) Long orderId, @RequestParam(name = "status", required = false) OrderStatus newStatus) throws InvalidInputException {
        LOGGER.info("New changeOrderStatus request with orderId=" + orderId);
        return new ResponseEntity<>(administratorServiceImpl.changeOrderStatus(orderId, newStatus), HttpStatus.OK);
    }

    @GetMapping("fetchOrders")
    public ResponseEntity<APIResponse<PandaOrder>> fetchOrdersForRestaurant(@RequestParam(name = "restaurantId", required = false) Long restaurantId) throws InvalidInputException {
        LOGGER.info("New fetchOrdersForRestaurant request with restaurantId=" + restaurantId);
        return new ResponseEntity<>(APIResponse.<PandaOrder>builder().response(administratorServiceImpl.fetchOrders(restaurantId)).build(), HttpStatus.OK);
    }


    @GetMapping("generatePDF")
    public void generateMenuPDF(@RequestParam(name = "adminId", required = false) Long adminId) {
        LOGGER.info("New generateMenuPDF request with adminId=" + adminId);
        administratorServiceImpl.generateMenuPDF(adminId);
    }

    @GetMapping("refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("New refreshToken request");
        userServiceImpl.refreshToken(request, response);
    }
}
