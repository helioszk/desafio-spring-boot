package br.com.alura.orders.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.alura.orders.modelo.Orders;

public class OrderForm {

	@NotNull @NotEmpty @Length(min = 5)
	private String description;
	@NotNull @NotEmpty @Length(min = 5)
	private String name;
	@NotNull
	private double total;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	
	public Orders converter() {
		return new Orders(description, name, total);
	}
	
}
