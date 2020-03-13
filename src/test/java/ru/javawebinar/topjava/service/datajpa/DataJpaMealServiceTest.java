package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void getWithUser() {
        Meal mealOfUser = service.getWithUser(MEAL1_ID, USER_ID);
        Assert.assertEquals(MEAL1, mealOfUser);
        Assert.assertEquals(USER, mealOfUser.getUser());

        Meal mealOfAdmin = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        Assert.assertEquals(ADMIN_MEAL1, mealOfAdmin);
        Assert.assertEquals(ADMIN, mealOfAdmin.getUser());
    }

    @Test
    public void getWithUserNotFound() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.get(1, USER_ID));
    }

    @Test
    public void getWithUserNotOwn() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithUser(MEAL1_ID, ADMIN_ID));

    }
}
