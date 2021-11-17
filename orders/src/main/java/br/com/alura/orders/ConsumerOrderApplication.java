package br.com.alura.orders;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import br.com.alura.orders.controller.OrderController;
import br.com.alura.orders.controller.form.AtualizacaoOrdersForm;

public class ConsumerOrderApplication {
	
	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		var consumer = new KafkaConsumer<String, String>(properties());
		consumer.subscribe(Collections.singletonList("ALURA_ORDERS"));

		while (true) {
			var records = consumer.poll(Duration.ofMillis(1000));
			if (!records.isEmpty()) {
				System.out.println("Found " + records.count() + " registries");

				for (var record : records) {
					System.out.println("-------------------------------------------------------");
					System.out.println("Processing new order...");
					System.out.println(record.key());
					System.out.println(record.value());

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
//					Long idPedido = null;
//					
//					if (!record.value().isEmpty() && record.value() != null) {
//						idPedido = Long.valueOf(record.value().substring(11, 12));
//					}
					
//					atualizarPedido(idPedido);
					
					System.out.println("Order processed!");
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private static void atualizarPedido(Long idPedido) {
		
		OrderController oc = new OrderController();
		
		oc.atualizar(idPedido, new AtualizacaoOrdersForm());
		
	}
	
	private static Properties properties() {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ConsumerOrderApplication.class.getSimpleName());

		return properties;
	}

}
