package ru.javawebinar.topjava.model;

import java.util.List;
import java.util.Optional;

public interface MealsData {

    public List<Meal> get();

    public Optional<Meal> getById(int id);
    public void create(Meal meal);
    public void delete(int id);
    public void update(Meal meal);

}
