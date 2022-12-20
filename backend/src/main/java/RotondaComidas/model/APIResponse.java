package RotondaComidas.model;

import lombok.*;

import java.util.List;

/**
 * A special type of Response that contains a list of entities. To be used for fetches, {@link RotondaComidas.service.impl.CustomerServiceImpl#fetchOrdersForCustomer(Long)} {@link RotondaComidas.service.impl.AdministratorServiceImpl#fetchOrders(Long)}
 *
 * @param <T> The type of entity, e.g.: {@link Restaurant} {@link Zone}
 */
@Getter
@Setter
@Builder
public class APIResponse<T> {

    private List<T> response;
}
