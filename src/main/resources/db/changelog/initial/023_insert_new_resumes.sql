--liquibase formatted sql
--changeset angryelizar:23-insert_new_resumes

INSERT INTO RESUMES(APPLICANT_ID, NAME, CATEGORY_ID, SALARY, IS_ACTIVE, CREATED_TIME, UPDATE_TIME)
VALUES ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Юрист со знанием английского языка',
        (select id from CATEGORIES where name = 'Продажи, обслуживание клиентов'), 200000, true, '2024-03-07T09:00',
        '2024-03-08T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Разработчик Java',
        (select id from CATEGORIES where name = 'IT'), 100000, true, '2024-02-15T09:00', '2024-02-16T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Менеджер по продажам',
        (select id from CATEGORIES where name = 'Продажи, обслуживание клиентов'), 80000, true, '2024-02-16T09:00',
        '2024-02-17T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Финансовый аналитик',
        (select id from CATEGORIES where name = 'Финансы, бухгалтерия'), 120000, true, '2024-02-17T09:00',
        '2024-02-18T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Дизайнер UX/UI',
        (select id from CATEGORIES where name = 'Искусство, развлечения, массмедиа'), 90000, true, '2024-02-18T09:00',
        '2024-02-19T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'HR-менеджер',
        (select id from CATEGORIES where name = 'Управление персоналом'), 85000, true, '2024-02-19T09:00',
        '2024-02-20T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Администратор систем',
        (select id from CATEGORIES where name = 'IT'), 110000, true, '2024-02-20T09:00', '2024-02-21T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Бухгалтер',
        (select id from CATEGORIES where name = 'Финансы, бухгалтерия'), 95000, true, '2024-02-21T09:00',
        '2024-02-22T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Архитектор',
        (select id from CATEGORIES where name = 'Архитектура, строительство'), 130000, true, '2024-02-22T09:00',
        '2024-02-23T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Маркетолог',
        (select id from CATEGORIES where name = 'Маркетинг'), 105000, true, '2024-02-23T09:00', '2024-02-24T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Автоэлектрик-диагност',
        (select id from CATEGORIES where name = 'Автомобильный бизнес'), 80000, true, '2024-02-24T09:00',
        '2024-02-25T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Frontend-разработчик',
        (select id from CATEGORIES where name = 'IT'), 95000, true, '2024-02-25T09:00', '2024-02-26T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Медицинская сестра',
        (select id from CATEGORIES where name = 'Медицина, фармацевтика'), 70000, true, '2024-02-26T09:00',
        '2024-02-27T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Системный администратор',
        (select id from CATEGORIES where name = 'IT'), 105000, true, '2024-02-27T09:00', '2024-02-28T09:00'),

       ((select id from USERS where EMAIL = 'conovalov.elizar@gmail.com'), 'Художник-иллюстратор',
        (select id from CATEGORIES where name = 'Искусство, развлечения, массмедиа'), 80000, true, '2024-02-28T09:00',
        '2024-02-29T09:00');



