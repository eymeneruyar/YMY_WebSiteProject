package YMY.repositories;

import YMY.entities.BoxActions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BoxActionsRepository extends JpaRepository<BoxActions,Integer> {

    //List of box actions by status and user id
    List<BoxActions> findByStatusEqualsAndUserIdEqualsAndTransactionDateBetween(boolean status, int userId, String transactionDateStart, String transactionDateEnd);

    //Verilen invoice id değerinin daha önce kayıtlı olup olmadığına bakar.
    boolean existsByStatusEqualsAndUserIdEqualsAndInvoice_IdEquals(boolean status, int userId, Integer id);

    //Verilen invoice değerine göre tablodaki boxActions nesnesinin döndürülmesi
    Optional<BoxActions> findByStatusEqualsAndUserIdEqualsAndInvoice_IdEquals(boolean status, int userId, Integer id);

    //List of box actions by payday to today
    List<BoxActions> findByStatusEqualsAndUserIdEqualsAndDateBetweenOrderByIdDesc(boolean status, int userId, String dateStart, String dateEnd);

    //List of box actions by description (giriş ve çıkış) and date
    List<BoxActions> findByUserIdEqualsAndStatusEqualsAndDescriptionEqualsAndTransactionDateBetween(int userId, boolean status, Integer description, String transactionDateStart, String transactionDateEnd);

    //List of box actions by description (tümü) and date
    List<BoxActions> findByUserIdEqualsAndStatusEqualsAndTransactionDateBetween(int userId, boolean status, String transactionDateStart, String transactionDateEnd);

    //List of box actions by description,date and company
    List<BoxActions> findByStatusEqualsAndUserIdEqualsAndDescriptionEqualsAndTransactionDateBetweenAndCompany_IdEquals(boolean status, int userId, Integer description, String transactionDateStart, String transactionDateEnd, Integer id);

    //Ödemelerin verilen tarih aralığı ve kasa tanımına göre getirilmesi
    List<BoxActions> findByStatusEqualsAndUserIdEqualsAndDescriptionEqualsAndTransactionDateBetween(boolean status, int userId, Integer description, String transactionDateStart, String transactionDateEnd);

    //Yapılan ödemenin geri alınmasıyla boxActions'da bulunan status değerinin false yapılması
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update box_actions set status = false where id = :id and user_id = :userId and status = true",nativeQuery = true)
    void updateStatusBoxActions(@Param("id") int id,@Param("userId") int userId);

}
