package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealRepository implements MealRepository {

    private final AtomicInteger counter;

    private final Map<Integer, Meal> meals;

    public MemoryMealRepository() {
        counter = new AtomicInteger(1);
        meals = new ConcurrentHashMap<>();
        insert(new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        insert(new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        insert(new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        insert(new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        insert(new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        insert(new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        insert(new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Optional<Meal> getById(int id) {
        return Optional.ofNullable(meals.get(id));
    }

    @Override
    public Meal insert(Meal meal) {
        meal.setId(counter.getAndIncrement());
        meals.putIfAbsent(meal.getId(), meal);
        return meals.get(meal.getId());
    }

    @Override
    public void delete(int id) {
        getById(id).ifPresent(value -> meals.remove(value.getId()));
    }

    @Override
    public Meal update(Meal meal) {
        meals.replace(meal.getId(), meal);
        return meals.get(meal.getId());
    }
}
