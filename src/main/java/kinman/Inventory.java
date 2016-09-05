package kinman;

import java.math.BigDecimal;

public class Inventory {

    private String name;

    private BigDecimal price;

    private int qty;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
