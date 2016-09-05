package kinman;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Autowired
    OrderService orderService;

	@Test
	public void contextLoads() {
	}

	@Test
    public void canCreateOrder() {
        Order order = new Order();

        List<OrderItem> orderItems = new ArrayList<>();

        OrderItem oranges = new OrderItem();
        oranges.setSku("Orange");
        oranges.setQty(3);

        orderItems.add(oranges);

        order.setOrderItems(orderItems);

        Order newOrder = orderService.create(order);

        assertNotNull((newOrder.getId()));
        assertEquals("open", newOrder.getStatus());

        OrderItem orderItem = newOrder.getOrderItems().get(0);
        assertEquals("Orange", orderItem.getSku());
        assertEquals("10.00", orderItem.getPrice().toString());
        assertEquals(3, orderItem.getQty());
        assertEquals("30.00", orderItem.getExtPrice().toString());
    }

}
