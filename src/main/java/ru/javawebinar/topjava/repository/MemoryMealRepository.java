package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealRepository implements MealRepository {
    private final AtomicInteger counter;

    private final Map<Integer, Meal> meals;

    public MemoryMealRepository() {
        counter = new AtomicInteger(1);
        meals = new ConcurrentHashMap<>();
        List<Meal> listMeal = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        listMeal.forEach(this::insert);
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
        int id = counter.getAndIncrement();
        meal.setId(id);
        meals.put(id, meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public Optional<Meal> update(Meal meal) {
        return meals.replace(meal.getId(), meal) != null ? Optional.of(meal) : Optional.empty();
    }
}
