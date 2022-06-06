package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.MealsData;
import ru.javawebinar.topjava.model.MealsDataMemory;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet("/meals")
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String LIST_MEAL = "meals.jsp";
    public static final String FORM_MEAL = "formMeal.jsp";
    public static final String URL_LIST_MEAL = "meals?action=list";

    private final MealsData mealsData;

    public MealServlet() {
        this.mealsData = new MealsDataMemory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action").toLowerCase();
        String forward;

        switch (action) {
            case "new":
                forward = FORM_MEAL;
                request.setAttribute("dateTime", LocalDateTime.now());
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            case "edit":
                forward = FORM_MEAL;
                int id = Integer.parseInt(request.getParameter("id"));
                Optional<Meal> meal = mealsData.getById(id);
                meal.ifPresent(value -> request.setAttribute("meal", value));
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            case "delete":
                mealsData.delete(Integer.parseInt(request.getParameter("id")));
                response.sendRedirect(URL_LIST_MEAL);
                break;
            default:
                forward = LIST_MEAL;
                listMeal(request, response);
                request.getRequestDispatcher(forward).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("save") != null) {
            Meal meal = new Meal();
            meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
            meal.setDescription(request.getParameter("description"));
            meal.setCalories(Integer.parseInt(request.getParameter("calories")));

            String id = request.getParameter("id");

            if (id == null || id.isEmpty()) {
                mealsData.insert(meal);
            } else {
                meal.setId(Integer.parseInt(id));
                mealsData.update(meal);
            }
        }

        response.sendRedirect(URL_LIST_MEAL);
    }

    private void listMeal(HttpServletRequest request, HttpServletResponse response) {
        List<Meal> meals = mealsData.getAll();
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, MealsDataMemory.CALORIES_PER_DAY);
        request.setAttribute("meals", mealsTo);
        request.setAttribute("dateTimeFormatter", dateTimeFormatter);
    }
}
