package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestDate {

    public static final int AUTH_USER_ID = START_SEQ;
    public static final int NOT_RIGHT = 100008;
    public static final int NOT_FOUND = 55;

    public static final List<Meal> mealsUser = Arrays.asList(
            new Meal(100003, LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0), "Завтрак User", 400),
            new Meal(100004, LocalDateTime.of(2022, Month.FEBRUARY, 1, 15, 0), "Обед User", 600),
            new Meal(100005, LocalDateTime.of(2022, Month.FEBRUARY, 1, 19, 0), "Ужин User", 500),
            new Meal(100006, LocalDateTime.of(2022, Month.FEBRUARY, 2, 8, 0), "Завтрак User", 1500),
            new Meal(100007, LocalDateTime.of(2022, Month.FEBRUARY, 2, 18, 0), "Ужин User", 1500));

    public static final List<Meal> mealsAdmin = Arrays.asList(
            new Meal(100008, LocalDateTime.of(2022, Month.FEBRUARY, 1, 10, 0), "Второй завтрак Admin", 400),
            new Meal(100009, LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0), "Бранч Admin", 600),
            new Meal(100010, LocalDateTime.of(2022, Month.FEBRUARY, 1, 19, 0), "Обед Admin", 2500));

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.now(), "Новая еда", 550);
    }

    public static Meal getUpdated(int i) {
        Meal updated = new Meal(mealsUser.get(1));
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription("Update meal");
        updated.setCalories(10);
        return updated;
    }

    public static Meal getUpdatedNotRight(int i) {
        Meal updated = new Meal(mealsAdmin.get(1));
        updated.setDateTime(LocalDateTime.now());
        updated.setDescription("Update meal");
        updated.setCalories(10);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("dateTime").isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("dateTime").isEqualTo(expected);
    }
}
