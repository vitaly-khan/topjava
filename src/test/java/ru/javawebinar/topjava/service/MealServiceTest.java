package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import sun.print.resources.serviceui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static org.assertj.core.api.Assertions.assertThat;


@ContextConfiguration({"/spring/spring-app.xml","/spring/spring-db.xml"})
@Sql("/db/populateDB.sql")
@RunWith(SpringRunner.class)
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Assert.assertEquals(MEAL2, service.get(USER_MEAL_ID, USER_ID));
        Assert.assertEquals(MEAL9, service.get(ADMIN_MEAL_ID, ADMIN_ID));
    }

    @Test(expected = NotFoundException.class)
    public void getSomeoneElses() {
        service.get(USER_MEAL_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotExisting() {
        service.get(NOT_EXISTING_USER_MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        try {
            service.delete(USER_MEAL_ID, USER_ID);
        } catch (Exception ex) {
            Assert.fail();
        }
        service.get(USER_MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteSomeoneElses() {
        service.delete(USER_MEAL_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotExisting() {
        service.delete(NOT_EXISTING_USER_MEAL_ID, USER_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        assertThat(service.getBetweenHalfOpen(MEAL2.getDate(), MEAL2.getDate(), USER_ID))
                .isEqualTo(Arrays.asList(MEAL4, MEAL3, MEAL2));
        assertThat(service.getBetweenHalfOpen(MEAL5.getDate(), null, USER_ID))
                .isEqualTo(Arrays.asList(MEAL8, MEAL7, MEAL6, MEAL5));
        assertThat(service.getBetweenHalfOpen(null, null, USER_ID))
                .isEqualTo(USER_MEALS);
    }

    @Test
    public void getAll() {
        assertThat(service.getAll(USER_ID)).isEqualTo(USER_MEALS);
    }

    @Test
    public void update() {
        Meal updatedMeal = getUpdatedMeal();
        service.update(updatedMeal, USER_ID);
        Assert.assertEquals(updatedMeal, service.get(updatedMeal.getId(), USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateSomeoneElses() {
        Meal updatedMeal = getUpdatedMeal();
        service.update(updatedMeal, ADMIN_ID);
    }

    private Meal getUpdatedMeal() {
        Meal updatedMeal = new Meal(MEAL2);
        updatedMeal.setCalories(333);
        updatedMeal.setDescription("Обновленный в тесте завтрак");
        return updatedMeal;
    }

    @Test
    public void create() {
        Meal expected = new Meal(null, LocalDateTime.now(), "Новая еда для теста", 777);
        Meal actual = service.create(expected, ADMIN_ID);
        Integer newId = actual.getId();
        expected.setId(newId);
        assertThat(actual).isEqualTo(expected);
        assertThat(service.get(newId, ADMIN_ID)).isEqualTo(expected);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDatetimeCreate() {
        Meal duplicate = new Meal(MEAL2);
        duplicate.setId(null);
        duplicate.setDescription("Тест на дублирование даты");
        service.create(duplicate, USER_ID);
    }
}

