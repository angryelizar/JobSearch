--liquibase formatted sql
--changeset angryelizar:20-insert_new_data

insert into USERS (NAME, SURNAME, EMAIL, PHONE_NUMBER, PASSWORD, AGE, ACCOUNT_TYPE, ENABLED)
values
    ( 'Елизар',  'Коновалов', 'conovalov.elizar@gmail.com', '+996708801562', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', 22, 'Соискатель', true),
    ('Attractor Software LLC', '', 'attractor@attractor.kg', '+996996996996', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', 8, 'Работодатель', true);

insert into ROLES (USER_ID, AUTHORITY_ID)
VALUES
    ( (select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), (select id from AUTHORITIES where AUTHORITY = 'APPLICANT') ),
    ( (select id from USERS where EMAIL = 'attractor@attractor.kg'), (select id from AUTHORITIES where AUTHORITY = 'EMPLOYER') );

insert into resumes (APPLICANT_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_TIME, UPDATE_TIME)
values ((select id from USERS where USERS.NAME = 'Елизар'), 'SMM-менеджер',
        (select id from CATEGORIES where CATEGORIES.NAME = 'Маркетинг'), 100000, true, '2024-03-08', '2024-03-08'),
       ((select id from USERS where USERS.NAME = 'Елизар'), 'Java-разработчик',
        (select id from CATEGORIES where CATEGORIES.NAME = 'IT'), 200000, true, '2024-03-08', '2024-03-08');


insert into VACANCIES (NAME, DESCRIPTION, CATEGORY_ID, SALARY, EXP_FROM, EXP_TO, IS_ACTIVE, AUTHOR_ID, CREATED_TIME,
                       UPDATE_TIME)
values ('SMM-специалист', 'Нам нужен классный SMM-специалист',
        (select id from CATEGORIES where CATEGORIES.NAME = 'Маркетинг'), 150000, 2, 5, true, (select id from USERS where USERS.NAME = 'Attractor Software LLC'), '2024-03-01',
        '2024-03-08'),
       ('Java-разработчик', 'Нам нужен классный разработчик', (select id from CATEGORIES where CATEGORIES.NAME = 'IT'),
        250000, 1, 2, true, (select id from USERS where USERS.NAME = 'Attractor Software LLC'), '2024-03-02', '2024-03-09');

insert into CONTACTS_INFO (TYPE_ID, RESUME_ID, content)
values ((select id from CONTACT_TYPES where TYPE = 'Telegram'), (select id from RESUMES where name like 'SMM-менеджер'), 't.me/angryelizar'),
       ((select id from CONTACT_TYPES where TYPE = 'Телефон'), (select id from RESUMES where name like 'Java-разработчик'), '+996708801562');

insert into WORK_EXPERIENCE_INFO (RESUME_ID, YEARS, COMPANY_NAME, POSITION, RESPONSIBILITIES)
values ((select id from RESUMES where name like 'SMM-менеджер'), 8, 'Интеллика', 'SMM-менеджер', 'Ведение проектов'),
       ((select id from RESUMES where name like 'Java-разработчик'), 2, 'Attractor School', 'Junior Java-developer',
        'Ассистент преподавателя');

insert into EDUCATION_INFO (RESUME_ID, INSTITUTION, PROGRAM, START_DATE, END_DATE, DEGREE)
values ((select id from RESUMES where name like 'SMM-менеджер'), 'Bold Brands Education', 'SMM за полгода', '2015-03-01', '2015-06-01',
        'SMM-менеджер'),
       ((select id from RESUMES where name like 'Java-разработчик'), 'Attractor School', 'Java за полгода', '2016-03-01', '2016-06-01',
        'Junior Java-developer');

insert into RESPONDED_APPLICANTS (RESUME_ID, VACANCY_ID, CONFIRMATION)
values ((select id from RESUMES where name like 'SMM-менеджер'), (select id from VACANCIES where name like 'SMM-специалист'), false),
       ((select id from RESUMES where name like 'Java-разработчик'), (select id from VACANCIES where name like 'Java-разработчик'), false);