package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Optional;

public interface MealRepository {
    List<Meal> getAll();

    Optional<Meal> getById(int id);

    void insert(Meal meal);

    void delete(int id);

    void update(Meal meal);
}
