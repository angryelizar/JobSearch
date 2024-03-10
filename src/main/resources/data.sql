-- Создаем пользователей (одного соискателя и одного работодателя)
-- insert into users (name, surname, email, password, account_type)
-- values
--     ( 'Елизар', 'Коновалов', 'elizar_01@inbox.ru', 'qwerty', 'Соискатель'),
--     ( 'Садыр', 'Жапаров', 'japarov@inbox.ru', 'qwerty', 'Работодатель');


-- Создаем категории
-- insert into CATEGORIES (NAME)
-- values ('Маркетинг'),
--        ('IT');


-- Создаем два резюме для соискателя
-- insert into resumes (APPLICANT_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_DATE, UPDATE_TIME)
-- values
--     ((select id from USERS where USERS.NAME = 'Елизар'), 'SMM-менеджер',
--         (select id from CATEGORIES where CATEGORIES.NAME = 'Маркетинг'), 100000, true, '2024-03-08', '2024-03-08'),
--     ((select id from USERS where USERS.NAME = 'Елизар'), 'Java-разработчик',
--      (select id from CATEGORIES where CATEGORIES.NAME = 'IT'), 200000, true, '2024-03-08', '2024-03-08');


-- Создаем несколько типов контактов
-- insert into CONTACT_TYPES (TYPE)
-- values
--     ( 'Телефон'),
--     ('Telegram'),
--     ('WhatsApp');

-- Создаем contact_info для двух резюме
-- insert into CONTACTS_INFO (TYPE_ID, RESUME_ID, "value")
-- values
--     ((select id from CONTACT_TYPES where TYPE = 'Telegram'), 2, 't.me/angryelizar'),
--     ((select id from CONTACT_TYPES where TYPE = 'Телефон'), 3, '+996708801562');