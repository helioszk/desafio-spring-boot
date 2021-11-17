package br.com.alura.orders.controller.form;

import br.com.alura.orders.modelo.OrderStatus;
import br.com.alura.orders.modelo.Orders;
import br.com.alura.orders.repository.OrderRepository;

public class AtualizacaoOrdersForm {

	private OrderStatus status;

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Orders atualizar(Long id, OrderRepository orderRepository) {
		Orders orders = orderRepository.getById(id);
		orders.setStatus(OrderStatus.PROCESSED);
		return orders;
	}

}
