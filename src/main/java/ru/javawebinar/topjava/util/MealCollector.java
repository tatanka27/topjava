package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MealCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>> {

    private final LocalTime startTime;
    private final LocalTime endTime;
    private final int caloriesPerDay;
    private final Map<LocalDate, Integer> sumCaloriesPerDay;

    public MealCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
        this.sumCaloriesPerDay = new HashMap<>();
    }

    @Override
    public Supplier<List<UserMeal>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
        return (userMeals, userMeal) -> {
            sumCaloriesPerDay.put(userMeal.getDateTime().toLocalDate(),
                    sumCaloriesPerDay.getOrDefault(userMeal.getDateTime().toLocalDate(), 0) + userMeal.getCalories());

            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMeals.add(userMeal);
            }
        };
    }

    @Override
    public BinaryOperator<List<UserMeal>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
        return userMeals -> userMeals.stream()
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), sumCaloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}
