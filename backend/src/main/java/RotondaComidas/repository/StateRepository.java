package RotondaComidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import RotondaComidas.model.OrderStatus;
import RotondaComidas.model.states.State;


public interface StateRepository extends JpaRepository<State, Long> {


    State findByOrderStatus(OrderStatus orderStatus);
}
