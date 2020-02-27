package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MealTestData {

    public static final int USER_MEAL_ID = 100_002;
    public static final int NOT_EXISTING_USER_MEAL_ID = 100_001;
    public static final int ADMIN_MEAL_ID = 100_009;

    public static final Meal MEAL2 = new Meal(USER_MEAL_ID, LocalDateTime.of(2020, 1, 30, 7, 0), "Завтрак", 700);
    public static final Meal MEAL3 = new Meal(USER_MEAL_ID + 1, LocalDateTime.of(2020, 1, 30, 12, 0), "Обед", 1000);
    public static final Meal MEAL4 = new Meal(USER_MEAL_ID + 2, LocalDateTime.of(2020, 1, 30, 19, 0), "Ужин", 300);
    public static final Meal MEAL5 = new Meal(USER_MEAL_ID + 3, LocalDateTime.of(2020, 1, 31, 0, 0), "Еда на граничное значение", 300);
    public static final Meal MEAL6 = new Meal(USER_MEAL_ID + 4, LocalDateTime.of(2020, 1, 31, 7, 0), "Завтрак", 300);
    public static final Meal MEAL7 = new Meal(USER_MEAL_ID + 5, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед", 1000);
    public static final Meal MEAL8 = new Meal(USER_MEAL_ID + 6, LocalDateTime.of(2020, 1, 31, 20, 0), "Ужин", 800);
    public static final Meal MEAL9 = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, 1, 31, 22, 0), "Ужин админа", 500);

    public static final List<Meal> USER_MEALS = Arrays.asList(MEAL8, MEAL7, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
}
