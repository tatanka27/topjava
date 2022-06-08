package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
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
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String LIST_MEAL = "meals.jsp";
    private static final String FORM_MEAL = "formMeal.jsp";
    private static final String URL_LIST_MEAL = "meals";

    private MealRepository mealRepository;

    @Override
    public void init() {
        this.mealRepository = new MemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet()");
        String action = request.getParameter("action");
        action = action != null ? action.toLowerCase() : "";
        String forward;

        switch (action) {
            case "new":
                log.debug("redirect to meals/new");
                forward = FORM_MEAL;
                request.setAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0));
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            case "edit":
                forward = FORM_MEAL;
                int id = getIdFromRequest(request);
                log.debug("redirect to meals/edit {}", id);
                mealRepository.getById(id).ifPresent(value -> request.setAttribute("meal", value));
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            case "delete":
                log.debug("redirect to meals/delete {}", getIdFromRequest(request));
                mealRepository.delete(getIdFromRequest(request));
                response.sendRedirect(URL_LIST_MEAL);
                break;
            default:
                log.debug("redirect to meals");
                forward = LIST_MEAL;
                List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealRepository.getAll(), LocalTime.MIN, LocalTime.MAX,
                        MealsUtil.CALORIES_PER_DAY);
                request.setAttribute("meals", mealsTo);
                request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
                request.getRequestDispatcher(forward).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("doPost()");
        request.setCharacterEncoding("UTF-8");

        Meal meal = new Meal(LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"), Integer.parseInt(request.getParameter("calories")));
        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            log.debug("insert()");
            mealRepository.insert(meal);
        } else {
            log.debug("update() {}", id);
            meal.setId(Integer.parseInt(id));
            mealRepository.update(meal);
        }
        response.sendRedirect(URL_LIST_MEAL);
    }

    private int getIdFromRequest(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }
}
