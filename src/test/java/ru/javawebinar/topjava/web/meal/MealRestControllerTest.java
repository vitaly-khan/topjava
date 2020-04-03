package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestMatcher;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.*;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_MEALS_URL_WITH_SLASH = MealRestController.REST_MEALS_URL + "/";

    @Autowired
    private MealService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_MEALS_URL_WITH_SLASH + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(MEAL1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_MEALS_URL_WITH_SLASH + MEAL1_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_MEALS_URL_WITH_SLASH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TestMatcher.usingFieldsComparator(MealTo.class).contentJson(
                        MealsUtil.getTos(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void createWithUri() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_MEALS_URL_WITH_SLASH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))

                .andExpect(status().isCreated());
        Meal created = TestUtil.readFromJson(resultActions, Meal.class);
        Integer id = created.getId();
        newMeal.setId(id);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(id, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_MEALS_URL_WITH_SLASH + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))

                .andDo(print())
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_MEALS_URL_WITH_SLASH +
                "between?start=2020-01-31T00:00&end=2020-01-31T13:00:00"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(TestMatcher.usingFieldsComparator(MealTo.class).contentJson(
                        MealsUtil.createTo(MEAL5, true),
                        MealsUtil.createTo(MEAL4, true)));
    }
}