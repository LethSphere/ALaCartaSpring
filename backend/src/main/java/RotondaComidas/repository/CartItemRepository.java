package RotondaComidas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import RotondaComidas.model.CartItem;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
