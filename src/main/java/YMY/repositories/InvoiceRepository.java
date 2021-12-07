package YMY.repositories;

import YMY.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    long countByStatusEqualsAndUserIdEqualsAndDateBetween(boolean status, int userId, String dateStart, String dateEnd);

    //Belirtilen tarih aralıklarında iş emir kayıtlarını getirir.
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd);

    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsAndBillingStatusEqualsOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd, Integer id, String billingStatus);

    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd, Integer id);

    List<Invoice> findByStatusEqualsAndUserIdEqualsAndCustomer_IdEqualsOrderByIdDesc(boolean status, int userId, Integer id);

    //Girilen tutar değerine göre kalan borcun, ödenen miktarın ve paid satus'ün güncellenmesi
    @Procedure(name = "ProcUpdateInvoiceDebtAndPaidStatus")
    void ProcUpdateInvoiceDebtAndPaidStatus(@Param("amount") float amount,@Param("status") boolean status,@Param("userId") int userId,@Param("invoiceId") int invoiceId);


}
