package YMY.repositories;

import YMY.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company,Integer> {

    List<Company> findByStatusEqualsAndUserIdEqualsOrderByIdAsc(boolean status, int userId);

}
