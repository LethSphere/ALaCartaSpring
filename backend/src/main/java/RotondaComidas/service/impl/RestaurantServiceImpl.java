package RotondaComidas.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import RotondaComidas.exception.InvalidInputException;
import RotondaComidas.model.Menu;
import RotondaComidas.model.Restaurant;
import RotondaComidas.repository.RestaurantRepository;
import RotondaComidas.service.services.RestaurantService;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantServiceImpl.class);

    @Autowired
    RestaurantRepository restaurantRepository;


    @Override
    public Menu fetchMenu(Long restaurantId) throws InvalidInputException {
        if (restaurantId == null)
            throw new InvalidInputException("Restaurant id cannot be null.");

        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> {
                    LOGGER.error("No restaurant found for restaurantId=" + restaurantId);
                    throw new RuntimeException("No restaurant found for restaurantId=" + restaurantId);
                }
        ).getMenu();
    }


    @Override
    public List<Restaurant> fetchRestaurants() {
        return restaurantRepository.findAll();
    }
}
