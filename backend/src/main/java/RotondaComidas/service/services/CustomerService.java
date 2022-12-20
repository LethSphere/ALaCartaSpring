package RotondaComidas.service.services;


import org.springframework.stereotype.Service;

import RotondaComidas.model.Customer;
import RotondaComidas.model.CustomerRegister;
import RotondaComidas.model.PandaOrder;
import RotondaComidas.model.DTOs.AccountDTO;

import java.util.List;


@Service
public interface CustomerService {

    Customer save(CustomerRegister customerRegister);
    Customer authenticate(AccountDTO accountDTO);
    PandaOrder placeOrder(Long restaurantId, Long customerId, String details, PandaOrder order);
    List<PandaOrder> fetchOrdersForCustomer(Long customerId);
    void sendMail(Customer customer, PandaOrder order, String details);
}
