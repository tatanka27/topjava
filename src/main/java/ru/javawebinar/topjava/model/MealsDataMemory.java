package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.Counter;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealsDataMemory implements MealsData {
    public static final int CALORIES_PER_DAY = 2000;

    private static Counter counter;

    private final CopyOnWriteArrayList<Meal> meals;

    public MealsDataMemory() {
        meals = new CopyOnWriteArrayList<>(Arrays.asList(
                new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
        Optional<Meal> m = findMaxId();
        counter = new Counter(m.map(meal -> meal.getId() + 1).orElse(0));
    }

    public List<Meal> getAll() {
        return meals;
    }

    @Override
    public Optional<Meal> getById(int id) {
        for (Meal meal : meals) {
            if (meal.getId() == id)
                return Optional.of(meal);
        }
        return Optional.empty();
    }

    @Override
    public void insert(Meal meal) {
        meal.setId(counter.getId());
        meals.add(meal);
    }

    @Override
    public void delete(int id) {
        Optional<Meal> meal = getById(id);
        meal.ifPresent(meals::remove);
    }

    @Override
    public void update(Meal meal) {
        Optional<Meal> oldMeal = getById(meal.getId());

        if (oldMeal.isPresent()) {
            oldMeal.get().setDescription(meal.getDescription());
            oldMeal.get().setCalories(meal.getCalories());
            oldMeal.get().setDateTime(meal.getDateTime());
        }
    }

    private Optional<Meal> findMaxId() {
        return meals.stream().max(Comparator.comparing(Meal::getId));
    }
}
