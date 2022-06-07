package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealRepository implements MealRepository {

    private final AtomicInteger counter;

    private final List<Meal> meals;

    public MemoryMealRepository() {
        counter = new AtomicInteger(1);
        meals = new CopyOnWriteArrayList<>();
        insert(new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        insert(new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        insert(new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        insert(new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        insert(new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        insert(new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        insert( new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
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
        meal.setId(counter.getAndIncrement());
        meals.add(meal);
    }

    @Override
    public void delete(int id) {
        getById(id).ifPresent(meals::remove);
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
}
