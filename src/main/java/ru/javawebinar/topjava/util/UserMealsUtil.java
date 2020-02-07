package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println(filteredByStreamsOptional(meals, LocalTime.of(7, 0), LocalTime.of(23, 0), 2000));
    }


    // This method requires changing UserMealWithExcess (or MealTo) : boolean -> AtomicBoolean
    // and constructor respectively
    //
    public static List<UserMealWithExcess> filteredByStreamsOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> sums = new HashMap<>();
        Map<LocalDate, AtomicBoolean> booleans = new HashMap<>();
        return meals.stream()
                .peek(x -> {
                    LocalDate date = x.getDateTime().toLocalDate();
                    sums.merge(date, x.getCalories(), Integer::sum);
                    booleans.putIfAbsent(date, new AtomicBoolean());
                    booleans.get(date).set(sums.get(date) > caloriesPerDay);
                })
                .filter(x->TimeUtil.isBetweenInclusive(x.getDateTime().toLocalTime(), startTime, endTime))
                .map(x -> new UserMealWithExcess(x.getDateTime(), x.getDescription(), x.getCalories(),
                        booleans.get(x.getDateTime().toLocalDate())))
                .collect(Collectors.toList());
    }
}
