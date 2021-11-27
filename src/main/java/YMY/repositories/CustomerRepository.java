package YMY.repositories;

import YMY.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    List<Customer> findByStatusAndUserIdOrderByIdAsc(boolean status, int userId);

}
