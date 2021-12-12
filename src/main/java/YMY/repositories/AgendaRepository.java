package YMY.repositories;

import YMY.entities.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda,Integer> {

    //List of notes by status and user in system
    List<Agenda> findByStatusEqualsAndUserIdEqualsOrderByIdDesc(boolean status, int userId);

}
