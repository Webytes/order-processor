package kinman;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ApplicationTests {
    @Autowired
    OrderService orderService;

    @Autowired
    InventoryService inventoryService;

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

        // confirm that inventory was adjusted
        Map<String, Inventory> inventory = inventoryService.inventoryForSkus(order.getSkus());
        assertEquals(97, inventory.get("Orange").getQty());
    }

    @Test
    public void orderInFailedStatusWhenInsufficientInventory() {
        Order order = new Order();

        List<OrderItem> orderItems = new ArrayList<>();

        OrderItem oranges = new OrderItem();
        oranges.setSku("Orange");
        oranges.setQty(101);

        orderItems.add(oranges);

        order.setOrderItems(orderItems);

        Order newOrder = orderService.create(order);

        assertEquals("failed", newOrder.getStatus());
    }

    @Test
    public void orderInFailedStatusWhenUnknownSku() {
        Order order = new Order();

        List<OrderItem> orderItems = new ArrayList<>();

        OrderItem item = new OrderItem();
        item.setSku("Grapefruit");
        item.setQty(1);

        orderItems.add(item);

        order.setOrderItems(orderItems);

        Order newOrder = orderService.create(order);

        assertEquals("failed", newOrder.getStatus());
    }

    @Test
    public void canCheckInventory() {
        List<String> skus = new ArrayList<>();
        skus.add("Orange");

        Map<String, Inventory> result = inventoryService.inventoryForSkus(skus);

        Inventory oranges = result.get("Orange");
        assertEquals(100, oranges.getQty());
        assertEquals("10.00", oranges.getPrice().toString());
    }

}
