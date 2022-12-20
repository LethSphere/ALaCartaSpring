package RotondaComidas.service.services;

import java.util.List;

import RotondaComidas.model.Menu;
import RotondaComidas.model.Restaurant;


public interface RestaurantService {

    Menu fetchMenu(Long restaurantId);
    List<Restaurant> fetchRestaurants();
}
