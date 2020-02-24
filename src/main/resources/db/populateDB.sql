DELETE FROM users;
DELETE FROM user_roles;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories) VALUES
(100000, '2020-01-30 07:00', 'Завтрак', 700),
(100000, '2020-01-30 12:00', 'Обед', 1000),
(100000, '2020-01-30 19:00', 'Ужин', 300),
(100000, '2020-01-31 00:00', 'Еда на граничное значение', 300),
(100000, '2020-01-31 07:00', 'Завтрак', 300),
(100000, '2020-01-31 13:00', 'Обед', 1000),
(100000, '2020-01-31 20:00', 'Ужин', 800),
(100001, '2020-01-31 14:00', 'Обед админа', 600),
(100001, '2020-01-31 22:00', 'Ужин админа', 500);
