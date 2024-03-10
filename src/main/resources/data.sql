-- insert into users (name, surname, email, password, account_type)
-- values
--     ( 'Елизар', 'Коновалов', 'elizar_01@inbox.ru', 'qwerty', 'Соискатель'),
--     ( 'Садыр', 'Жапаров', 'japarov@inbox.ru', 'qwerty', 'Работодатель');

-- insert into CATEGORIES (NAME)
-- values ('Маркетинг'),
--        ('IT');

-- insert into resumes (APPLICANT_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_DATE, UPDATE_TIME)
-- values
--     ((select id from USERS where USERS.NAME = 'Елизар'), 'SMM-менеджер',
--         (select id from CATEGORIES where CATEGORIES.NAME = 'Маркетинг'), 100000, true, '2024-03-08', '2024-03-08'),
--     ((select id from USERS where USERS.NAME = 'Елизар'), 'Java-разработчик',
--      (select id from CATEGORIES where CATEGORIES.NAME = 'IT'), 200000, true, '2024-03-08', '2024-03-08');
