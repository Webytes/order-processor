package kinman;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT)
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    @Id
    @GeneratedValue
    private long id;

    private String status;

    private Date createdAt = new Date();

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL})
    private List<OrderItem> orderItems;

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

//    public void addOrderItem(OrderItem orderItem) {
//        orderItems.add(orderItem);
//        orderItem.setOrder(this);
//    }
//
//    public void removeOrderItem(OrderItem orderItem) {
//        orderItems.remove(orderItem);
//        orderItem.setOrder(null);
//    }

    String cursorValue() {
        String padded = String.format("%9d", id);
        return Base64.getEncoder().encodeToString(padded.getBytes());
    }

    static Long idFromCursor(String cursor) {
        try {
            String value = new String(Base64.getDecoder().decode(cursor), "utf-8");
            return Long.valueOf(value.trim());
        } catch (UnsupportedEncodingException e) {
            return 0L;
        }
    }

}
