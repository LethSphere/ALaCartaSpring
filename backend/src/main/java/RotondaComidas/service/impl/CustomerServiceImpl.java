package RotondaComidas.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import RotondaComidas.exception.DuplicateEntryException;
import RotondaComidas.exception.InvalidInputException;
import RotondaComidas.model.*;
import RotondaComidas.model.DTOs.AccountDTO;
import RotondaComidas.model.states.State;
import RotondaComidas.repository.*;
import RotondaComidas.service.services.CustomerService;
import RotondaComidas.service.utils.Validator;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;


@Service
public class CustomerServiceImpl implements CustomerService {

    private final static Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class);

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PandaOrderRepository pandaOrderRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    StateRepository stateRepository;

    @Autowired
    UserRepository userRepository;

    Validator validator = Validator.getInstance();

    @Override
    public Customer save(CustomerRegister customerRegister) throws InvalidInputException {
        if (customerRegister == null || customerRegister.getUser().getEmail() == null || customerRegister.getUser().getPassword() == null || customerRegister.getCustomer().getName() == null || customerRegister.getCustomer().getAddress() == null || customerRegister.getCustomer().getAddressZone() == null || customerRegister.getCustomer().getAddressZone().getId() == null)
            throw new InvalidInputException("You request body is not a valid Customer object. Please refer to the documentation!");
        if (!validator.isEmailValid(customerRegister.getUser().getEmail()))
            throw new InvalidInputException("Email is not valid! It should be a valid email(eg. foodpanda@glovo.com");
        if (!validator.isPasswordValid(customerRegister.getUser().getPassword()))
            throw new InvalidInputException("Password does not meet the requirements\n-At least 8 characters long\n-At least a digit\nAt least a letter");
        if (customerRegister.getCustomer().getName().isEmpty())
            throw new InvalidInputException("Customer {email} must not be null");
        if (customerRegister.getCustomer().getAddress().isEmpty())
            throw new InvalidInputException("Customer {email} must not be null");

        try {
            User _user = userRepository.save(
                    User
                            .builder()
                            .email(customerRegister.getUser().getEmail())
                            .password(BCrypt.hashpw(customerRegister.getUser().getPassword(), BCrypt.gensalt()))
                            .build());
            Customer _customer = customerRepository.save(
                    Customer
                            .builder()
                            .name(customerRegister.getCustomer().getName())
                            .address(customerRegister.getCustomer().getAddress())
                            .addressZone(customerRegister.getCustomer().getAddressZone())
                            .user(_user)
                            .build()
            );
            LOGGER.info("New customer saved with customerId=" + _customer.getCustomerId());
            return _customer;
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateEntryException("Email is already registered! Try to login");
        }
    }


    @Override
    public Customer authenticate(AccountDTO accountDTO) throws InvalidInputException {
        if (accountDTO == null || accountDTO.getCredential() == null || accountDTO.getPassword() == null)
            throw new InvalidInputException("You request body is not a valid Customer object. Please refer to the documentation!");

        User _user = userRepository.findByEmail(accountDTO.getCredential()).orElseThrow(
                () -> new InvalidInputException("Invalid credentials")
        );
        if (BCrypt.checkpw(accountDTO.getPassword(), _user.getPassword())) {
            return customerRepository.findByUser(_user).orElseThrow(
                    () -> new InvalidInputException("Are you trying to log in as an admin?")
            );
        } else throw new InvalidInputException("Invalid credentials");
    }


    @Override
    public PandaOrder placeOrder(Long restaurantId, Long customerId, String details, PandaOrder order) throws InvalidInputException {
        if (restaurantId == null)
            throw new InvalidInputException("Required request parameter {restaurantId} cannot be null or missing");
        if (customerId == null)
            throw new InvalidInputException("Required request parameter {customerId} cannot be null or missing");
        if (order == null || order.getProducts() == null)
            throw new InvalidInputException("You request body is not a valid PandaOrder object. Please refer to the documentation!");
        if (order.getProducts().size() == 0)
            throw new InvalidInputException("The order's list of {product} is empty (size == 0)");

        Customer _customer = customerRepository.findById(customerId).orElseThrow(
                () -> {
                    LOGGER.error("No customer found for customerId=" + customerId);
                    throw new InvalidInputException("No customer found for customerId=" + customerId);
                }
        );

        Restaurant _restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> {
                    LOGGER.error("No restaurant found for restaurantId=" + restaurantId);
                    throw new InvalidInputException("No restaurant found for restaurantId=" + restaurantId);
                }
        );

        if (!_restaurant.getDeliveryZones().contains(_customer.getAddressZone()))
            throw new InvalidInputException("The restaurant " + _restaurant.getName() + " doesn't deliver to your zone at the moment");

        State _pendingState = stateRepository.findByOrderStatus(OrderStatus.PENDING);

        PandaOrder _pandaOrder = pandaOrderRepository.save(
                PandaOrder
                        .builder()
                        .customer(_customer)
                        .products(order.getProducts())
                        .restaurant(_restaurant)
                        .state(_pendingState)
                        .restaurantName(_restaurant.getName())
                        .build()
        );

        for (CartItem cartItem : order.getProducts()) {
            Food _foodItem = foodRepository.findById(cartItem.getItem().getFoodId()).orElseThrow(
                    () -> new InvalidInputException("No Food found for foodId=" + cartItem.getItem().getFoodId())
            );
            CartItem _item = cartItemRepository.save(
                    CartItem
                            .builder()
                            .item(_foodItem)
                            .quantity(cartItem.getQuantity())
                            .order(_pandaOrder)
                            .build()
            );
        }
        LOGGER.info("New pandaOrder placed with pandaOrderId=" + _pandaOrder.getOrderId());
        if (_customer.getUser().getEmail().equals("ivan.alexandru20@yahoo.com"))
            sendMail(_customer, _pandaOrder, details);
        return _pandaOrder;
    }


    @Override
    public List<PandaOrder> fetchOrdersForCustomer(Long customerId) {
        return pandaOrderRepository.findAllByCustomer_CustomerId(customerId);
    }

    @Override
    public void sendMail(Customer customer, PandaOrder order, String details) {
        String to = order.getRestaurant().getAdministrator().getUser().getEmail();

        String from = customer.getUser().getEmail();


        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", "smtp.mail.yahoo.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.ssl", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(customer.getUser().getEmail(), "pvizhbyjyyrpyvye");
            }

        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("New Order!");

            double total = order.getProducts()
                    .stream()
                    .mapToDouble(cartItem -> cartItem.getQuantity() * cartItem.getItem().getPrice())
                    .sum();
            StringBuilder content = new StringBuilder();
            content
                    .append("New order for your restaurant{")
                    .append(order.getRestaurant().getName())
                    .append("}\n\n\n")
                    .append("Products:\n");
            for (CartItem product : order.getProducts()) {
                String name = product.getItem().getName();
                String quantity = product.getQuantity().toString();
                content.append(quantity)
                        .append(" x ")
                        .append(name)
                        .append('\n');
            }
            content
                    .append("--------\nTotal: ")
                    .append(total)
                    .append("\n--------\nAddress: ")
                    .append(customer.getAddress())
                    .append("\n--------\nDetails: ")
                    .append(details);
            message.setText(content.toString());

            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            LOGGER.error("Unable to send mail to the administrator of restaurant: " + order.getRestaurant());
        }
    }
}
