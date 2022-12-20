package RotondaComidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import RotondaComidas.model.PandaOrder;

import java.util.List;


public interface PandaOrderRepository extends JpaRepository<PandaOrder, Long> {

    List<PandaOrder> findAllByCustomer_CustomerId(Long customerId);
}
