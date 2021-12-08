package YMY.repositories;

import YMY.entities.BoxActions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoxActionsRepository extends JpaRepository<BoxActions,Integer> {

    //Verilen invoice id değerinin daha önce kayıtlı olup olmadığına bakar.
    boolean existsByStatusEqualsAndUserIdEqualsAndInvoice_IdEquals(boolean status, int userId, Integer id);

    //Verilen invoice değerine göre tablodaki boxActions nesnesinin döndürülmesi
    Optional<BoxActions> findByStatusEqualsAndUserIdEqualsAndInvoice_IdEquals(boolean status, int userId, Integer id);

    //List of box actions by payday to today
    List<BoxActions> findByStatusEqualsAndUserIdEqualsAndDateBetweenOrderByIdDesc(boolean status, int userId, String dateStart, String dateEnd);





}
