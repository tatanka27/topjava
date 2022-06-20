DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id)
VALUES ('2022-02-01T09:00:00', 'Завтрак User', 400, 100000),
       ('2022-02-01T15:00:00', 'Обед User', 600, 100000),
       ('2022-02-01T19:00:00', 'Ужин User', 500, 100000);

INSERT INTO meals (datetime, description, calories, user_id)
VALUES ('2022-02-01T10:00:00', 'Второй завтрак Admin', 400, 100001),
       ('2022-02-01T18:00:00', 'Бранч Admin', 600, 100001),
       ('2022-02-02T19:00:00', 'Обед Admin', 2500, 100001);