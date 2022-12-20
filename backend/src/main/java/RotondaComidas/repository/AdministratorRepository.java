package RotondaComidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import RotondaComidas.model.Administrator;
import RotondaComidas.model.User;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {


    Optional<Administrator> findByUser(User user);
}
