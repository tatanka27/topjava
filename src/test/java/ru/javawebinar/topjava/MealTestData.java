package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_ID = START_SEQ + 3;
    public static final int ADMIN_MEAL_ID = MEAL_ID + 6;
    public static final int NOT_FOUND = 55;

    public static Meal userMeal6 = new Meal(MEAL_ID + 5, LocalDateTime.of(2022, Month.FEBRUARY, 2, 18, 0), "Ужин User", 1500);
    public static Meal userMeal5 = new Meal(MEAL_ID + 4, LocalDateTime.of(2022, Month.FEBRUARY, 2, 8, 0), "Завтрак User", 1500);
    public static Meal userMeal4 = new Meal(MEAL_ID + 3, LocalDateTime.of(2022, Month.FEBRUARY, 2, 0, 0), "Еда на граничное значение", 100);
    public static Meal userMeal3 = new Meal(MEAL_ID + 2, LocalDateTime.of(2022, Month.FEBRUARY, 1, 19, 0), "Ужин User", 500);
    public static Meal userMeal2 = new Meal(MEAL_ID + 1, LocalDateTime.of(2022, Month.FEBRUARY, 1, 15, 0), "Обед User", 600);
    public static Meal userMeal1 = new Meal(MEAL_ID, LocalDateTime.of(2022, Month.FEBRUARY, 1, 9, 0), "Завтрак User", 400);


    public static Meal adminMeal3 = new Meal(MEAL_ID +8 , LocalDateTime.of(2022, Month.FEBRUARY, 1, 19, 0), "Обед Admin", 2500);
    public static Meal adminMeal2 = new Meal(MEAL_ID + 7, LocalDateTime.of(2022, Month.FEBRUARY, 1, 18, 0), "Бранч Admin", 600);
    public static Meal adminMeal1 = new Meal(MEAL_ID + 6, LocalDateTime.of(2022, Month.FEBRUARY, 1, 10, 0), "Второй завтрак Admin", 400);


    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2022, Month.JUNE, 21, 15, 0), "Новая еда", 550);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal1);
        updated.setDateTime(LocalDateTime.of(2022, Month.FEBRUARY, 3, 15, 0));
        updated.setDescription("Update meal");
        updated.setCalories(10);
        return updated;
    }

    public static Meal getUpdatedAdminMeal() {
        Meal updated = new Meal(adminMeal1);
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
