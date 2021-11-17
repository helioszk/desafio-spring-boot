package br.com.alura.orders.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.orders.controller.dto.OrderDto;
import br.com.alura.orders.controller.form.AtualizacaoOrdersForm;
import br.com.alura.orders.controller.form.OrderForm;
import br.com.alura.orders.modelo.Orders;
import br.com.alura.orders.repository.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderRepository orderRepository;

	@GetMapping
	public List<OrderDto> lista() {
		List<Orders> orders = orderRepository.findAll();
		return OrderDto.converter(orders);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<OrderDto> create(@RequestBody @Valid OrderForm form, UriComponentsBuilder uriBuilder)
			throws InterruptedException, ExecutionException {

		Orders orders = form.converter();
		orderRepository.save(orders);

		@SuppressWarnings("resource")
		var producer = new KafkaProducer<String, String>(properties());
		var value = orders.toString();
		var record = new ProducerRecord<>("ALURA_ORDERS", value, value);

		producer.send(record, (data, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
				return;
			}
			System.out.println("sucesso enviando " + data.topic() + ":::partition " + data.partition() + "/ offset "
					+ data.offset() + "/ timestamp " + data.timestamp());
		}).get();

		URI uri = uriBuilder.path("/orders/{id}").buildAndExpand(orders.getId()).toUri();
		return ResponseEntity.created(uri).body(new OrderDto(orders));
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
		Optional<Orders> order = orderRepository.findById(id);
		if (order.isPresent()) {
			return ResponseEntity.ok(new OrderDto(order.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Orders> optional = orderRepository.findById(id);
		if (optional.isPresent()) {
			orderRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<OrderDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoOrdersForm form){
		Optional<Orders> optional = orderRepository.findById(id);
		if(optional.isPresent()) {
			Orders orders = form.atualizar(id, orderRepository);
			return ResponseEntity.ok(new OrderDto(orders));	
		}
		
		return ResponseEntity.notFound().build();
		
		
	}
	
	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return properties;
	}
}
