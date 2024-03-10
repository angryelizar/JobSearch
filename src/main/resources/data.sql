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

-- Создаем опыт работы для двух резюме
-- insert into WORK_EXPERIENCE_INFO (RESUME_ID, YEARS, COMPANY_NAME, POSITION, RESPONSIBILITIES)
-- values
--     (2, 8, 'Интеллика', 'SMM-менеджер', 'Ведение проектов'),
--     (3, 2, 'Attractor School', 'Junior Java-developer', 'Ассистент преподавателя');

-- Создаем места обучения для двух резюме
-- insert into EDUCATION_INFO (RESUME_ID, INSTITUTION, PROGRAM, START_DATE, END_DATE, DEGREE)
-- values
--     (2, 'Bold Brands Education', 'SMM за полгода',  '2015-03-01', '2015-06-01', 'SMM-менеджер'),
--     (3, 'Attractor School', 'Java за полгода',  '2016-03-01', '2016-06-01', 'Junior Java-developer');