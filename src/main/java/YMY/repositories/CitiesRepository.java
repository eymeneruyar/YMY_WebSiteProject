package YMY.repositories;

import YMY.entities.Cities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitiesRepository extends JpaRepository<Cities,Integer> {

    Optional<Cities> findByCityKey(int cityKey);

}
