package RotondaComidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import RotondaComidas.model.Customer;
import RotondaComidas.model.User;

import java.util.Optional;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findById(Long idAccount);
    Optional<Customer> findByUser(User user);

}
