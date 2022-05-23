package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                        "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                        "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                        "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                        "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                        "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                        "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                        "Ужин", 410),
                new UserMeal(LocalDateTime.of(2020, Month.MARCH, 31, 11, 0),
                        "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0),
                LocalTime.of(13, 0), 1000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0),
                LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> sumCaloriesPerDay = new HashMap<>();
        for (UserMeal meal : meals) {
            int calories = sumCaloriesPerDay.getOrDefault(meal.getDateTime().toLocalDate(), 0);
            sumCaloriesPerDay.put(meal.getDateTime().toLocalDate(), calories + meal.getCalories());
        }

        List<UserMealWithExcess> resultMealWithExcess = new ArrayList<>();
        for (UserMeal meal : meals) {
            int calories = sumCaloriesPerDay.get(meal.getDateTime().toLocalDate());

            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                resultMealWithExcess.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), calories > caloriesPerDay));
            }
        }
        return resultMealWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> resultMeals = meals.stream().filter(x -> (x.getDateTime().toLocalTime().isAfter(startTime)
                        || x.getDateTime().toLocalTime().equals(startTime))
                        && x.getDateTime().toLocalTime().isBefore(endTime))
                .sorted(Comparator.comparing(UserMeal::getDateTime).reversed())
                .collect(Collectors.toList());

        Map<LocalDate, Integer> excessCalories = meals.stream()
                .collect(groupingBy(x -> x.getDateTime().toLocalDate(), summingInt(UserMeal::getCalories)));

        List<UserMealWithExcess> resultMealWithExcess = resultMeals.stream().map(x -> new UserMealWithExcess(x.getDateTime(),
                x.getDescription(), x.getCalories(),
                excessCalories.get(x.getDateTime().toLocalDate()) > caloriesPerDay)).collect(Collectors.toList());
        return resultMealWithExcess;
    }
}
