package ara.main.Repositories.InventoryRepository;

import ara.main.Entity.PersonEntities.persons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ara.main.Entity.InventoryEntities.Product;

import java.util.Optional;

@Repository

public interface PoductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByUsername(String product);
}
