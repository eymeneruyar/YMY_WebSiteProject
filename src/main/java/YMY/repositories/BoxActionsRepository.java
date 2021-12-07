package YMY.repositories;

import YMY.entities.BoxActions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxActionsRepository extends JpaRepository<BoxActions,Integer> {

    //Verilen invoice id değerinin daha önce kayıtlı olup olmadığına bakar.
    boolean existsByStatusEqualsAndUserIdEqualsAndInvoice_IdEquals(boolean status, int userId, Integer id);

    //Amount değerini güncelleme

}
