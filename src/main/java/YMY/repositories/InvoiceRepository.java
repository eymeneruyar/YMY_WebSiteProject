package YMY.repositories;

import YMY.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    long countByStatusEqualsAndUserIdEqualsAndDateBetween(boolean status, int userId, String dateStart, String dateEnd);

    //Belirtilen tarih aralıklarında iş emir kayıtlarını getirir.
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd);

    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsAndBillingStatusEqualsOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd, Integer id, String billingStatus);

    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd, Integer id);



}
