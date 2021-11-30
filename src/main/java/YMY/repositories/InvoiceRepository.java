package YMY.repositories;

import YMY.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    long countByDateBetweenAndStatusEqualsAndUserIdEquals(String dateStart, String dateEnd, boolean status, int userId);

    long countByDateBetweenAndStatusEqualsAndUserIdEqualsAndDateContains(String dateStart, String dateEnd, boolean status, int userId, String date);

}
