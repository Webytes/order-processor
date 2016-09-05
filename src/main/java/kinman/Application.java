package kinman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	@Autowired
    OrderRepository orderRepository;

    @Bean
    OrderService orderService() { return new OrderService(orderRepository); }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
