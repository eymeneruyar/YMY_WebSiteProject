package YMY.repositories;

import YMY.entities.MonthlyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlyGoalRepository extends JpaRepository<MonthlyGoal,Integer> {

    //Get the monthly goal value by key value
    Optional<MonthlyGoal> findByStatusEqualsAndUserIdEqualsAndKeyEquals(boolean status, int userId, String key);

}
