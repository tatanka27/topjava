package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final int NOT_RIGHT = 100009;
    public static final int NOT_FOUND = 55;

    public static Meal meal6User = new Meal(100008, LocalDateTime.of(2022, Month.FEBRUARY, 2, 18, 0), "Ужин User", 1500);
    public static Meal meal5User = new Meal(100007, LocalDateTime.of(2022, Month.FEBRUARY, 2, 8, 0), "Завтрак User", 1500);
    public static Meal meal4User = new Meal(100006, LocalDateTime.of(2022, Month.FEBRUARY, 2, 0, 0), "Еда на граничное значение", 100);
    public static Meal meal3User = new Meal(100005, LocalDateTime.of(2022, Month.FEBRUARY, 1, 19, 0), "Ужин User", 500);
    public static Meal meal2User = new Meal(100004, LocalDateTime.of(2022, Month.FEBRUARY, 1, 15, 0), "Обед User", 600);
    public static Meal meal1User = new Meal(100003, LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0), "Завтрак User", 400);


    public static Meal meal3Admin = new Meal(100011, LocalDateTime.of(2022, Month.FEBRUARY, 1, 19, 0), "Обед Admin", 2500);
    public static Meal meal2Admin = new Meal(100010, LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0), "Бранч Admin", 600);
    public static Meal meal1Admin = new Meal(100009, LocalDateTime.of(2022, Month.FEBRUARY, 1, 10, 0), "Второй завтрак Admin", 400);


    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.JUNE, 21, 15, 0), "Новая еда", 550);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1User);
        updated.setDateTime(LocalDateTime.of(2022, Month.FEBRUARY, 3, 15, 0));
        updated.setDescription("Update meal");
        updated.setCalories(10);
        return updated;
    }

    public static Meal getUpdatedNotRight() {
        Meal updated = new Meal(meal1Admin);
        updated.setDateTime(LocalDateTime.of(2022, Month.FEBRUARY, 3, 15, 0));
        updated.setDescription("Update meal");
        updated.setCalories(10);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
