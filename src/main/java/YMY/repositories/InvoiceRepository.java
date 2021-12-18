package YMY.repositories;

import YMY.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    long countByStatusEqualsAndUserIdEqualsAndDateBetween(boolean status, int userId, String dateStart, String dateEnd);

    //Belirtilen tarih aralıklarında iş emir kayıtlarını getirir.
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd);

    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsAndBillingStatusEqualsOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd, Integer id, String billingStatus);

    List<Invoice> findByStatusEqualsAndUserIdEqualsAndDateBetweenAndCompany_IdEqualsOrderByInvoiceCodeDesc(boolean status, int userId, String dateStart, String dateEnd, Integer id);

    List<Invoice> findByStatusEqualsAndUserIdEqualsAndCustomer_IdEqualsOrderByIdDesc(boolean status, int userId, Integer id);

    //List of debtor customers
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsOrderByIdDesc(boolean status, int userId, boolean paidStatus);

    //List of invoice by company id
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndCompany_IdEquals(boolean status, int userId, boolean paidStatus, Integer id);

    //List of invoice for total paid selected company
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndCompany_IdEquals(boolean status, int userId, Integer id);

    //List of invoice by selected company id
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndCompany_IdEqualsOrderByIdDesc(boolean status, int userId, Integer id);

    //List of invoice by filtered company id,date,paid status and billing status
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndBillingStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(boolean status, int userId, boolean paidStatus, String billingStatus, Integer id, String dateStart, String dateEnd);

    //List of invoice by filtered date and company id
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(boolean status, int userId, Integer id, String dateStart, String dateEnd);

    //List of invoice by filtered date, company id and billing status
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndBillingStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(boolean status, int userId, String billingStatus, Integer id, String dateStart, String dateEnd);

    //List of invoice by filtered date, company id and paid status
    List<Invoice> findByStatusEqualsAndUserIdEqualsAndPaidStatusEqualsAndCompany_IdEqualsAndDateBetweenOrderByIdDesc(boolean status, int userId, boolean paidStatus, Integer id, String dateStart, String dateEnd);

    //Number of total work
    int countByStatusEqualsAndUserId(boolean status, int userId);

    //Girilen tutar değerine göre kalan borcun, ödenen miktarın  güncellenmesi
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update invoice set remaining_debt = remaining_debt - :amount_in, paid = paid + :amount_in where user_id = :user_id_in and id = :invoice_id_in and status = :status_in and remaining_debt >= 0 and paid <= debt and :amount_in <= remaining_debt;",nativeQuery = true)
    void updateRemainingDebtAndPaid(@Param("amount_in") float amount_in,@Param("status_in") boolean status_in,@Param("user_id_in") int user_id_in,@Param("invoice_id_in")int invoice_id_in);

    //Remaining_debt ve paid verilerine göre paid satus'ün güncellenmesi
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update invoice set paid_status = true where user_id = :user_id_in and id = :invoice_id_in and status = :status_in and remaining_debt = 0 and paid = debt;",nativeQuery = true)
    void updatePaidStatus(@Param("status_in") boolean status_in,@Param("user_id_in") int user_id_in,@Param("invoice_id_in")int invoice_id_in);

    //Yapılan bir ödeme işleminin geri alınması
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update invoice SET paid = paid - :amount, paid_status = false, remaining_debt = remaining_debt + :amount where status = :status and user_id = :userId and id = :invoiceId and paid >= 0 and remaining_debt <= invoice.debt and remaining_debt >= 0;", nativeQuery = true)
    void undoPayment(@Param("amount") float amount, @Param("status") boolean status,@Param("userId") int userId, @Param("invoiceId") int invoiceId);

}
