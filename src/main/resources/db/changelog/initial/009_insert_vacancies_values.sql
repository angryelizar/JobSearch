insert into VACANCIES (NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, AUTHOR_ID, CREATED_TIME,
                       UPDATE_TIME)
values ('SMM-специалист', 'Нам нужен классный SMM-специалист',
        (select id from CATEGORIES where CATEGORIES.NAME = 'Маркетинг'), 150000, 2, 5, true, 2, '2024-03-01',
        '2024-03-08'),
       ('Java-разработчик', 'Нам нужен классный разработчик', (select id from CATEGORIES where CATEGORIES.NAME = 'IT'),
        250000, 1, 2, true, 2, '2024-03-02', '2024-03-09');