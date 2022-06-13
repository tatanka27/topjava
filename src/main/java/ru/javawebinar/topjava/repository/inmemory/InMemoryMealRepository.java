package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        } else if (!meal.getUserId().equals(userId)) {
            return null;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Meal meal = repository.get(id);
        if (meal != null) {
            if (meal.getUserId() == userId) {
                repository.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Meal meal = repository.get(id);
        if (meal != null) {
            if (meal.getUserId() == userId) {
                return meal;
            }
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return repository.values().stream()
                .filter(meal -> meal.getUserId().equals(userId))
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

