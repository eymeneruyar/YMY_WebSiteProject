package YMY.repositories;

import YMY.entities.Services;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicesRepositories extends JpaRepository<Services,Integer> {

    List<Services> findByStatusEquals(boolean status);

}
