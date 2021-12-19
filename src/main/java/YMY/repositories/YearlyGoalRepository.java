package YMY.repositories;

import YMY.entities.YearlyGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YearlyGoalRepository extends JpaRepository<YearlyGoal,Integer> {

    //Get the yearly goal value by key value
    Optional<YearlyGoal> findByStatusEqualsAndUserIdEqualsAndKeyEquals(boolean status, int userId, String key);

}
