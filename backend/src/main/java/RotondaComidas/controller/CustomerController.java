package RotondaComidas.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import RotondaComidas.exception.InvalidInputException;
import RotondaComidas.model.*;
import RotondaComidas.model.DTOs.AccountDTO;
import RotondaComidas.service.impl.CustomerServiceImpl;
import RotondaComidas.service.impl.RestaurantServiceImpl;
import RotondaComidas.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("customer")
public class CustomerController {

    private final Logger LOGGER = LogManager.getLogger(CustomerController.class);

    @Autowired
    CustomerServiceImpl customerServiceImpl;

    @Autowired
    RestaurantServiceImpl restaurantServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping("register")
    public ResponseEntity<Customer> register(@RequestBody(required = false) CustomerRegister customerRegister) {
        LOGGER.info("New register request with email=" + customerRegister.getUser().getEmail());
        return new ResponseEntity<>(customerServiceImpl.save(customerRegister), HttpStatus.CREATED);
    }

    @PostMapping("auth")
    public ResponseEntity<Customer> authenticate(@RequestBody(required = false) AccountDTO accountDTO) {
        LOGGER.info("New authentication request with email=" + accountDTO.getCredential());
        return new ResponseEntity<>(customerServiceImpl.authenticate(accountDTO), HttpStatus.OK);
    }


    @PostMapping("placeOrder")
    public ResponseEntity<PandaOrder> placeOrder(@RequestParam(name = "restaurantId", required = false) Long restaurantId, @RequestParam(name = "customerId", required = false) Long customerId, @RequestParam(name = "details", required = false) String details, @RequestBody(required = false) PandaOrder order) throws InvalidInputException {
        LOGGER.info("New placeOrder request with restaurantId=" + restaurantId + " and customerId=" + customerId);
        return new ResponseEntity<>(customerServiceImpl.placeOrder(restaurantId, customerId, details, order), HttpStatus.CREATED);
    }


    @GetMapping("fetchRestaurants")
    public ResponseEntity<APIResponse<Restaurant>> fetchRestaurants() throws InvalidInputException {
        return new ResponseEntity<>(APIResponse.<Restaurant>builder().response(restaurantServiceImpl.fetchRestaurants()).build(), HttpStatus.OK);
    }

 
    @GetMapping("fetchOrders")
    public ResponseEntity<APIResponse<PandaOrder>> fetchOrdersForCustomer(@RequestParam(name = "customerId") Long customerId) throws InvalidInputException {
        LOGGER.info("New fetchOrdersForCustomer request with customerId=" + customerId);
        return new ResponseEntity<>(APIResponse.<PandaOrder>builder().response(customerServiceImpl.fetchOrdersForCustomer(customerId)).build(), HttpStatus.OK);
    }


    @GetMapping("refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("New refreshToken request");
        userServiceImpl.refreshToken(request, response);
    }
}
