--liquibase formatted sql
--changeset angryelizar:07_insert_work_experience_info_values

insert into WORK_EXPERIENCE_INFO (RESUME_ID, YEARS, COMPANY_NAME, POSITION, RESPONSIBILITIES)
values ((select id from RESUMES where name like 'SMM-менеджер'), 8, 'Интеллика', 'SMM-менеджер', 'Ведение проектов'),
       ((select id from RESUMES where name like 'Java-разработчик'), 2, 'Attractor School', 'Junior Java-developer',
        'Ассистент преподавателя');