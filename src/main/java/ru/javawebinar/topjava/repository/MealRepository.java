package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

//@Repository
public interface MealRepository {
    // null if not found, when updated
    Meal save(int userId, Meal meal);

    // false if not found
    boolean delete(int userId, int id);

    // null if not found
    Meal get(int userId, int id);

    List<Meal> getAll(int userId);

    List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate);
}
