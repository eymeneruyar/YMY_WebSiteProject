package YMY.repositories;

import YMY.entities.Works;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorksRepository extends JpaRepository<Works,Integer> {

    @Query(value = "select * from works as w where w.status = ? and w.user_id = ? and w.ınvoice_id = ? ORDER BY w.id ASC", nativeQuery = true)
    List<Works> findAllByWorks(boolean status,int user_id,int ınvoice_id);

}
