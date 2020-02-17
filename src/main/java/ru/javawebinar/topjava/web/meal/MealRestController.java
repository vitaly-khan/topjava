package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) {
        int userId = authUserId();
        ValidationUtil.checkNew(meal);
        log.info("Create: UserId={}, MealId={}", userId, meal.getId());
        return service.create(userId, meal);
    }

    public void update(Meal meal, int mealId) {
        int userId = authUserId();
        ValidationUtil.assureIdConsistent(meal, mealId);
        log.info("Update: UserId={}, MealId={}", userId, meal.getId());
        service.update(userId, meal);
    }

    public void delete(int mealId) {
        int userId = authUserId();
        log.info("Delete: UserId = {}, MealId={}", userId, mealId);
        service.delete(userId, mealId);
    }

    public Meal get(int mealId) {
        int userId = authUserId();
        log.info("Get: UserId = {}, MealId={}", userId, mealId);
        return service.get(userId, mealId);
    }

    public List<MealTo> getAll() {
        int userId = authUserId();
        log.info("GetAll: UserId={}", userId);
        return MealsUtil.getTos(service.getAll(userId), authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        int userId = authUserId();
        log.info("GetFiltered: startdate='{}', enddate='{}', starttime='{}', endtime='{}'",
                startDate, endDate, startTime, endTime);
        return MealsUtil.getFilteredTos(service.getFiltered(userId, startDate, endDate), authUserCaloriesPerDay(),
                startTime, endTime);
    }
}