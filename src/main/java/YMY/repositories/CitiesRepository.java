package YMY.repositories;

import YMY.entities.Cities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitiesRepository extends JpaRepository<Cities,Integer> {
}
