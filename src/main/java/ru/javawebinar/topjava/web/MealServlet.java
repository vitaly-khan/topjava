package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealDao;
import ru.javawebinar.topjava.repository.MealDao;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static MealDao mealDao = new InMemoryMealDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String datetime = request.getParameter("datetime");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");
        Meal meal = new Meal(id.equals("") ? null : Integer.valueOf(id),
                LocalDateTime.parse(datetime, DATE_TIME_FORMATTER),
                description, Integer.parseInt(calories));
        mealDao.save(meal);
        response.sendRedirect("meals");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");

        if (action == null) {
            log.info("forward to meals.jsp");
            request.setAttribute("meals",
                    MealsUtil.filteredByStreams(
                            mealDao.getAll(),
                            LocalTime.MIN,
                            LocalTime.MAX,
                            MealsUtil.CALORIES_PER_DAY));
            getServletContext().getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }
        Meal meal;
        switch (action) {
            case "create":
                meal = new Meal(null, LocalDateTime.now(), "", 500);
                break;
            case "edit":
                meal = mealDao.get(Integer.parseInt(id));
                break;
            case "delete":
                if (id != null) {
                    log.info("redirect to meals.jsp");
                    mealDao.delete(Integer.parseInt(id));
                    response.sendRedirect("meals");
                }
                return;
            default:
                throw new IllegalStateException("Wrong action with meal!");
        }
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("edit.jsp").forward(request, response);
    }
}
