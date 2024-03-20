--liquibase formatted sql
--changeset angryelizar:04_insert_resumes_values

insert into resumes (APPLICANT_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_TIME, UPDATE_TIME)
values ((select id from USERS where USERS.NAME = 'Елизар'), 'SMM-менеджер',
        (select id from CATEGORIES where CATEGORIES.NAME = 'Маркетинг'), 100000, true, '2024-03-08', '2024-03-08'),
       ((select id from USERS where USERS.NAME = 'Елизар'), 'Java-разработчик',
        (select id from CATEGORIES where CATEGORIES.NAME = 'IT'), 200000, true, '2024-03-08', '2024-03-08');