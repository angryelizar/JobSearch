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

-- Создаем две вакансии от работодателя
-- insert into VACANCIES (NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, AUTHOR_ID, CREATED_DATE, UPDATE_TIME)
-- values
--     ('SMM-специалист', 'Нам нужен классный SMM-специалист', (select id from CATEGORIES where CATEGORIES.NAME = 'Маркетинг'), 150000, 2, 5, true, 2, '2024-03-01', '2024-03-08'),
--     ('Java-разработчик', 'Нам нужен классный разработчик', (select id from CATEGORIES where CATEGORIES.NAME = 'IT'), 250000, 1, 2, true, 2, '2024-03-02', '2024-03-09');

-- Добавим записи в таблицу «кто откликнулся», чтобы показать, что соискатель откликнулся на вакансии
-- insert into RESPONDED_APPLICANTS (RESUME_ID, VACANCY_ID, CONFIRMATION)
-- values
--     (2, 1, true),
--     (3, 2, true);