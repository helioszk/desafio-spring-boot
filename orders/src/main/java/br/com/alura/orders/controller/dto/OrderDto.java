package br.com.alura.orders.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.alura.orders.modelo.OrderStatus;
import br.com.alura.orders.modelo.Orders;

public class OrderDto {

	private Long id;
	private String description;
	private String name;
	private double total;
	private OrderStatus status;

	public OrderDto(Orders orders) {
		this.id = orders.getId();
		this.description = orders.getDescription();
		this.name = orders.getName();
		this.total = orders.getTotal();
		this.status = orders.getStatus();
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public double getTotal() {
		return total;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public static List<OrderDto> converter(List<Orders> orders) {
		return orders.stream().map(OrderDto::new).collect(Collectors.toList());
	}

}
