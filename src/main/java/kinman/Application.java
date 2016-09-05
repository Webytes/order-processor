package kinman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class Application {

	@Autowired
    OrderRepository orderRepository;

    @Autowired
    DataSource dataSource;

    @Bean
    OrderService orderService() { return new OrderService(orderRepository, inventoryService()); }

    @Bean
    InventoryService inventoryService() { return new InventoryService(dataSource); }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
