package ru.javawebinar.topjava.model;

import java.util.List;
import java.util.Optional;

public interface MealsData {

    List<Meal> getAll();

    Optional<Meal> getById(int id);

    void insert(Meal meal);

    void delete(int id);

    void update(Meal meal);
}
