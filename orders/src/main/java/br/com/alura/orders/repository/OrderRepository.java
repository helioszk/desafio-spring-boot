package br.com.alura.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.orders.modelo.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long>{

}
