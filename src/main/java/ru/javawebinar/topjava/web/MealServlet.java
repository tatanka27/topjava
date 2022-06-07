package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String LIST_MEAL = "meals.jsp";
    public static final String FORM_MEAL = "formMeal.jsp";
    public static final String URL_LIST_MEAL = "meals";

    private MealRepository mealRepository;

    public void init() {
        this.mealRepository = new MemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String forward;

        if (action == null) {
            forward = LIST_MEAL;
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealRepository.getAll(), LocalTime.MIN, LocalTime.MAX,
                    User.CALORIES_PER_DAY);
            request.setAttribute("meals", mealsTo);
            request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
            request.getRequestDispatcher(forward).forward(request, response);
        } else {
            switch (action.toLowerCase()) {
                case "new":
                    forward = FORM_MEAL;
                    request.setAttribute("meal", new Meal(0, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0));
                    request.getRequestDispatcher(forward).forward(request, response);
                    break;
                case "edit":
                    forward = FORM_MEAL;
                    int id = getIdFromRequest(request);
                    mealRepository.getById(id).ifPresent(value -> request.setAttribute("meal", value));
                    request.getRequestDispatcher(forward).forward(request, response);
                    break;
                case "delete":
                    mealRepository.delete(getIdFromRequest(request));
                    response.sendRedirect(URL_LIST_MEAL);
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        int id = getIdFromRequest(request);
        if (id == 0) {
            mealRepository.insert(new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"), Integer.parseInt(request.getParameter("calories"))));
        } else {
            mealRepository.update(new Meal(id, LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"), Integer.parseInt(request.getParameter("calories"))));
        }
        response.sendRedirect(URL_LIST_MEAL);
    }

    private int getIdFromRequest(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}
