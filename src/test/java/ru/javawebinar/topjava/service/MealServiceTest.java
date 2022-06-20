package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestDate;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestDate.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(100003, AUTH_USER_ID);
        assertMatch(meal, MealTestDate.mealsUser.get(0));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, AUTH_USER_ID));
    }

    @Test
    public void getNoRight() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_RIGHT, AUTH_USER_ID));
    }

    @Test
    public void delete() {
        service.delete(100005, AUTH_USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(100005, AUTH_USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, AUTH_USER_ID));
    }

    @Test
    public void deleteNoRight() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_RIGHT, AUTH_USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2022, Month.FEBRUARY, 1);
        LocalDate endDate =  LocalDate.of(2022, Month.FEBRUARY, 1);
        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, AUTH_USER_ID);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(AUTH_USER_ID);
        assertMatch(meals, mealsUser.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed()).collect(Collectors.toList()));
    }

    @Test
    public void update() {
        Meal updated = getUpdated(1);
        service.update(updated, AUTH_USER_ID);
        assertMatch(service.get(updated.getId(), AUTH_USER_ID), getUpdated(1));
    }

    @Test
    public void updateNotRight() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdatedNotRight(0), AUTH_USER_ID));
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, AUTH_USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, AUTH_USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0), "Новый завтрак User", 444),
                        AUTH_USER_ID));
    }
}