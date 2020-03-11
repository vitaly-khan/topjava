package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

//@NoRepositoryBean
@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
//    @Transactional
//    @Modifying
//    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
//    int delete(@Param("id") int id, @Param("user_id") int user_id);

//    @Query("SELECT m FROM Meal m WHERE m.user.id=:id ORDER BY m.dateTime DESC")
//    List<Meal> findByUserIdOrderByDateTimeDesc(@Param("id") int userId);

//    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId " +
//            "AND m.dateTime >= :start AND m.dateTime < :end ORDER BY m.dateTime DESC")
//    List<Meal> findFilteredAndSorted(
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end,
//            @Param("userId") int userId);
//
    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    List<Meal> findByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> findAllByUserIdAndDateTimeIsGreaterThanEqualAndDateTimeBeforeOrderByDateTimeDesc(int userId,
            LocalDateTime start, LocalDateTime end);

    Meal findByIdAndUserId(int id, int user_id);
}
