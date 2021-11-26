package YMY.repositories;

import YMY.entities.Towns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TownsRepository extends JpaRepository<Towns,Integer> {

    List<Towns> findByTownCityKeyOrderByIdAsc(Integer townCityKey);

}
