package kinman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService {
    private JdbcTemplate template;

    @Autowired
    public InventoryService(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    public Map<String, Inventory> inventoryForSkus(List<String> skus) {
        Map<String, Inventory> result = new HashMap<>();

        if (skus.isEmpty()) {
            return result;
        }

        String query = "select sku, price, qty from inventory where sku in (?) ";

        template.query(
                query, skus.toArray(), (rs, rowNum) -> {
                    Inventory inventory = new Inventory(rs.getString("sku"),
                            rs.getBigDecimal("price"),
                            rs.getInt("qty"));

                    result.put(inventory.getSku(), inventory);
                    return inventory;
                });


        return result;
    }

    public void decrementInventory(String sku, int qty) {
        String stmt = "update inventory set qty = qty - ? where sku = ?";

        template.update((connection -> {
            PreparedStatement ps = connection.prepareStatement(stmt);
            ps.setInt(1, qty);
            ps.setString(2, sku);

            return ps;
        }));

    }
}