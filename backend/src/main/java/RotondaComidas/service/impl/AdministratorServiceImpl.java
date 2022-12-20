package RotondaComidas.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import RotondaComidas.exception.DuplicateEntryException;
import RotondaComidas.exception.InsufficientArgumentsException;
import RotondaComidas.exception.InvalidCredentialsException;
import RotondaComidas.exception.InvalidInputException;
import RotondaComidas.model.*;
import RotondaComidas.model.DTOs.AccountDTO;
import RotondaComidas.model.states.State;
import RotondaComidas.repository.*;
import RotondaComidas.service.services.AdministratorService;
import RotondaComidas.service.utils.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AdministratorServiceImpl implements AdministratorService {

    private final static Logger LOGGER = LogManager.getLogger(AdministratorServiceImpl.class);

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PandaOrderRepository pandaOrderRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private UserRepository userRepository;

   
    Validator validator = Validator.getInstance();



    @Override
    public Administrator saveAdministrator(AccountDTO accountDTO) throws InvalidInputException {
        if (accountDTO == null || accountDTO.getCredential() == null || accountDTO.getPassword() == null)
            throw new InvalidInputException("You request body is not a valid Administrator object. Please refer to the documentation!");
        if (!validator.isEmailValid(accountDTO.getCredential()))
            throw new InvalidInputException("Email is not valid! It should be a valid email(eg. foodpanda@glovo.com");
        if (!validator.isPasswordValid(accountDTO.getPassword()))
            throw new InvalidInputException("Password does not meet the requirements\n-At least 8 characters long\n-At least a digit\nAt least a letter");


        try {
            User _user = userRepository.save(
                    User
                            .builder()
                            .email(accountDTO.getCredential())
                            .password(BCrypt.hashpw(accountDTO.getPassword(), BCrypt.gensalt()))
                            .build());

            Administrator _admin = administratorRepository.save(
                    Administrator
                            .builder()
                            .user(_user)
                            .build());

            LOGGER.info("New administrator saved with adminId=" + _admin.getAdminId());
            return _admin;
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateEntryException("Email is already registered! Try to login");
        }

    }

  
    @Override
    public Administrator authenticate(AccountDTO accountDTO) throws InvalidCredentialsException {
        if (accountDTO == null || accountDTO.getCredential() == null || accountDTO.getPassword() == null)
            throw new InvalidInputException("You request body is not a valid Administrator object. Please refer to the documentation!");


        User _user = userRepository.findByEmail(accountDTO.getCredential()).orElseThrow(
                () -> new InvalidCredentialsException("Invalid credentials")
        );

        if (BCrypt.checkpw(accountDTO.getPassword(), _user.getPassword())) {
            Optional<Administrator> _adminOpt = administratorRepository.findByUser(_user);
            if (_adminOpt.isPresent()) {
                Administrator _admin = _adminOpt.get();
                _admin.getUser().setPassword("********");
                return _admin;
            } else
                throw new InvalidInputException("Are you trying to log in as a user?");
        } else throw new InvalidInputException("Invalid credentials");
    }


    @Override
    public Restaurant addRestaurant(Long adminId, Restaurant restaurant) throws InvalidInputException {
        Administrator _admin = administratorRepository.findById(adminId).orElseThrow(
                () -> new RuntimeException("No administrator found for adminId=" + adminId)
        );

        if (_admin.getRestaurant() != null) {
            LOGGER.error("Restaurant was already added for adminId=" + adminId);
            throw new RuntimeException("Restaurant already added for this administrator account");
        } else {
            if (restaurant == null || restaurant.getName() == null || restaurant.getLocation() == null || restaurant.getLocationZone().getId() == null)
                throw new InvalidInputException("You request body is not a valid Restaurant object. Please refer to the documentation!");
            if (restaurant.getName().isEmpty() || restaurant.getLocation().isEmpty())
                throw new InvalidInputException("Restaurant {name} and {location} cannot be empty");

            if (restaurant.getDeliveryZones() == null) {
                restaurant.setDeliveryZones(new ArrayList<Zone>() {{
                    add(restaurant.getLocationZone());
                }});
            } else if (!restaurant.getDeliveryZones().contains(restaurant.getLocationZone()))
                restaurant.getDeliveryZones().add(restaurant.getLocationZone());

            try {
                Restaurant _restaurant = restaurantRepository.save(
                        Restaurant
                                .builder()
                                .name(restaurant.getName())
                                .location(restaurant.getLocation())
                                .locationZone(restaurant.getLocationZone())
                                .deliveryZones(restaurant.getDeliveryZones())
                                .administrator(_admin)
                                .build()
                );
                Menu _menu = menuRepository.save(
                        Menu
                                .builder()
                                .restaurant(_restaurant)
                                .build()
                );
                for (CategoryType type : CategoryType.values()) {
                    categoryRepository.save(
                            Category
                                    .builder()
                                    .category(type)
                                    .menu(_menu)
                                    .build()
                    );
                }
                LOGGER.info("New restaurant added for adminId=" + adminId + " with restaurantId=" + _restaurant.getRestaurantId());
                return _restaurant;
            } catch (DataIntegrityViolationException dataIntegrityViolationException) {
                throw new DuplicateEntryException("Name of the restaurant is already taken!");
            }
        }
    }


    @Override
    public Food addFoodForCategory(Long categoryId, Food food) throws RuntimeException {
        if (categoryId == null)
            throw new InsufficientArgumentsException("No category selected! Please select one first!");
        if (food == null || food.getName() == null || food.getDescription() == null || food.getPrice() == null)
            throw new InvalidInputException("You request body is not a valid Food object. Please refer to the documentation!");
        if (food.getName().isEmpty())
            throw new InvalidInputException("Food {name} cannot be empty");
        if (food.getDescription().isEmpty())
            throw new InvalidInputException("Food {description} cannot be empty");

        Category _category = categoryRepository.findById(categoryId).orElseThrow(
                () -> {
                    LOGGER.error("No category found for categoryId=" + categoryId);
                    throw new RuntimeException("No category found for categoryId=" + categoryId);
                }
        );
        food.setCategory(_category);

        Food _food =
                foodRepository.save(
                        Food
                                .builder()
                                .name(food.getName())
                                .description(food.getDescription())
                                .price(food.getPrice())
                                .category(_category)
                                .build()
                );

        LOGGER.info("New food added for categoryId=" + categoryId + " with foodId=" + _food.getCategory());
        return _food;
    }


    @Override
    public PandaOrder changeOrderStatus(Long orderId, OrderStatus orderStatus) throws InvalidInputException {
        if (orderId == null)
            throw new InvalidInputException("Required request parameter {orderId} cannot be null or missing");
        if (orderStatus == null)
            throw new InvalidInputException("Required request parameter {orderStatus} cannot be null or missing");

        PandaOrder _pandaOrder = pandaOrderRepository.findById(orderId).orElseThrow(
                () -> {
                    LOGGER.error("No PandaOrder found for orderId=" + orderId);
                    throw new InvalidInputException("No PandaOrder found for orderId=" + orderId);
                }
        );

        State _state = _pandaOrder.getState();
        OrderStatus _newStatus = _state.changeState(orderStatus);
        State _newState = stateRepository.findByOrderStatus(_newStatus);

        _pandaOrder.setState(_newState);
        pandaOrderRepository.save(_pandaOrder);
        LOGGER.info("Status changed for orderId=" + orderId + " from " + _state.getOrderStatus() + " into " + _newState.getOrderStatus());
        return _pandaOrder;
    }

    public void generateMenuPDF(Long adminId) {

        if (adminId == null)
            throw new InvalidInputException("The required parameter {adminId} cannot be null");

        Administrator _admin = administratorRepository.findById(adminId).orElseThrow(
                () -> {
                    LOGGER.error("No administrator found for adminId= + adminId");
                    throw new InvalidInputException("No administrator found for adminId=" + adminId);
                }
        );

        Menu _menu = _admin.getRestaurant().getMenu();

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(_admin.getRestaurant().getName() + " menu.pdf"));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

            for (Category category : _menu.getCategories()) {
                Paragraph chunk = new Paragraph(category.getCategory() + "\n");
                document.add(chunk);
                for (Food food : category.getFoodList()) {
                    Paragraph fod = new Paragraph("\tName: " + food.getName() + "\n\tDescription: " +
                            food.getDescription() + "\n\tPrice: " + food.getPrice() + "\n\n");
                    document.add(fod);
                }
            }

            document.close();
        } catch (FileNotFoundException | DocumentException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public List<PandaOrder> fetchOrders(Long restaurantId) throws InvalidInputException {
        if (restaurantId == null)
            throw new InvalidInputException("Required request parameter {restaurantId} cannot be null or missing");

        Restaurant _restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new InvalidInputException("No restaurant found for restaurantId=" + restaurantId)
        );

        return _restaurant.getOrders();
    }
}
