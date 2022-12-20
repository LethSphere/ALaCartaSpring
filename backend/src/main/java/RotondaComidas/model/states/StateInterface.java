package RotondaComidas.model.states;

import RotondaComidas.model.OrderStatus;

public interface StateInterface {

    OrderStatus changeState(OrderStatus newStatus);
}
