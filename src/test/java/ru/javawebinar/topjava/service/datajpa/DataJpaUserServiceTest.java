package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeal() {
        User userWithMeal = service.getWithMeal(USER_ID);
        User adminWithMeal = service.getWithMeal(ADMIN_ID);
        Assert.assertEquals(MEALS, userWithMeal.getMeals());
        Assert.assertEquals(List.of(ADMIN_MEAL2, ADMIN_MEAL1), adminWithMeal.getMeals());
    }

    @Test
    public void getWithMealNotFound() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithMeal(1));
    }

    @Test
    public void getWithEmptyMealList() {
        User userWithNoMeal = service.getWithMeal(USER_WITHOUT_MEAL_ID);
        Assert.assertTrue(userWithNoMeal.getMeals().isEmpty());
    }
}
