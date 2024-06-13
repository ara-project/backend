package ara.main.Repositories;

import ara.main.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,String> {
    List<Orders> findAllByIdentification(String id);
}
