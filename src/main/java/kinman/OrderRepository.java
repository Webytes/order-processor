package kinman;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order order);

    List<Order> findByAccount(Account account);

    Order findByAccountIdAndId(long accountId, long id);
}

