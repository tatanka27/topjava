package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final Map<Integer, List<Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (!meal.getUserId().equals(userId)) {
            return null;
        }

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            List<Meal> list = repository.get(meal.getUserId());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(meal);
            repository.put(meal.getUserId(), list);
            return meal;
        }
        // handle case: update, but not present in storage
        Meal m = repository.get(meal.getUserId()).get(meal.getId());
        if (m != null) {
            repository.get(meal.getUserId()).set(meal.getId(), meal);
        } else {
            return null;
        }
        return m;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Meal meal = repository.get(userId).get(id);
        if (meal != null) {
            repository.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        return repository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        return getAllByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAllFilterByDate");
        return getAllByPredicate(userId, meal -> DateTimeUtil.isBetweenHalClose(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getAllByPredicate(int userId, Predicate<Meal> filter) {
        return repository.get(userId).stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

