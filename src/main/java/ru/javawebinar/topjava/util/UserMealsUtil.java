package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

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

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        SortedSet<UserMeal> resultMeals = new TreeSet<>(
                Comparator.comparing(UserMeal::getDateTime))
                .descendingSet();

        Map<LocalDate, Integer> excessCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            if ((meal.getDateTime().toLocalTime().isAfter(startTime)
                    || meal.getDateTime().toLocalTime().equals(startTime))
                    && meal.getDateTime().toLocalTime().isBefore(endTime)) {
                resultMeals.add(meal);
            }
            int calories = excessCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0);
            calories = calories + meal.getCalories();
            excessCalories.put(meal.getDateTime().toLocalDate(), calories);
        }

        List<UserMealWithExcess> resultMealWithExcess = new ArrayList<>();
        for (UserMeal resultMeal : resultMeals) {
            int calories = excessCalories.get(resultMeal.getDateTime().toLocalDate());
            if (calories > caloriesPerDay) {
                resultMealWithExcess.add(new UserMealWithExcess(resultMeal.getDateTime(), resultMeal.getDescription(),
                        resultMeal.getCalories(), true));
            } else {
                resultMealWithExcess.add(new UserMealWithExcess(resultMeal.getDateTime(), resultMeal.getDescription(),
                        resultMeal.getCalories(), false));
            }
        }

        return resultMealWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }
}
