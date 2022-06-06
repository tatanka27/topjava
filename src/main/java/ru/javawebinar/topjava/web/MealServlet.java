package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.MealsData;
import ru.javawebinar.topjava.model.MealsDataMemory;
import ru.javawebinar.topjava.util.Counted;
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
    public static final String LIST_MEAL = "meals?action=list";

    private MealsData mealsData;

    public MealServlet() {
        this.mealsData = new MealsDataMemory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //log.debug("redirect to meals");

        String action = request.getParameter("action");

        switch (action) {
            case "new":
                log.debug("showNewForm");
                showNewForm(request, response);
                break;
            case "insert":
                log.debug("insert");
                insertMeal(request, response);
                break;
            case "edit":
                log.debug("showEditForm");
                showEditForm(request, response);
                break;
            case "update":
                log.debug("update");
                updateMeal(request, response);
                break;
            case "delete":
                deleteMeal(request, response);
                break;
            default:
                log.debug("list");
                listMeal(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("save") != null) {
            doGet(request, response);

        } else if (request.getParameter("cancel") != null) {
            response.sendRedirect(LIST_MEAL);
        }
    }

    private void listMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Meal> meals = mealsData.get();
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, MealsDataMemory.CALORIES_PER_DAY);
        request.setAttribute("meals", mealsTo);
        request.setAttribute("dateTimeFormatter", dateTimeFormatter);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("formMeal.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Optional<Meal> meal = mealsData.getById(id);
        meal.ifPresent(value -> request.setAttribute("meal", value));

        request.getRequestDispatcher("formMeal.jsp").forward(request, response);
    }

    private void insertMeal(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Meal meal = new Meal();
        meal.setId(new Counted().getId());
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        mealsData.create(meal);

        response.sendRedirect(LIST_MEAL);
    }

    private void deleteMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mealsData.delete(Integer.parseInt(request.getParameter("id")));
        response.sendRedirect(LIST_MEAL);
    }

    private void updateMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Meal meal = new Meal();
        int id = Integer.parseInt(request.getParameter("id"));
        meal.setId(id);
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        mealsData.update(meal);

        response.sendRedirect(LIST_MEAL);
    }
}
