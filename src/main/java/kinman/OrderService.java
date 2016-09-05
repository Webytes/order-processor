package kinman;

import java.math.BigDecimal;

public class OrderService {
    private OrderRepository repo;

    public OrderService(OrderRepository orderRepository) { this.repo = orderRepository; }

    public Order create(Order order) {
        // TODO: check availability and set status accordingly
        String status = "open";

        // initialize system-maintained properties
        order.setStatus(status);
        for (OrderItem item : order.getOrderItems()) {
            // TODO: Look up price in InventoryService
            item.setPrice(new BigDecimal("10.00"));
            item.setExtPrice(item.getPrice().multiply(new BigDecimal(item.getQty())));
            item.setOrder(order);
        }

        repo.save(order);

        return order;
    }
}
