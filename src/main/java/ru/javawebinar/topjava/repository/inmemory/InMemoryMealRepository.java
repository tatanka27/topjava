package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals1.forEach(meal -> save(meal, 1));
        MealsUtil.meals2.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
            return meal;
        }

        Map<Integer, Meal> meals = repository.get(userId);
        // handle case: update, but not present in storage
        if (meals == null) {
            return null;
        }

        if (meals.containsKey(meal.getId())) {
            meals.put(meal.getId(), meal);
            return meal;
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null ? meals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return getAllByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllFilterByDate");
        return getAllByPredicate(userId, meal -> DateTimeUtil.isBetweenHalClosed(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getAllByPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null ? meals.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList()) : Collections.emptyList();
    }
}

