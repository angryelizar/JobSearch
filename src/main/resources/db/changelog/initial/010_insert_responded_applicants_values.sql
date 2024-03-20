--liquibase formatted sql
--changeset angryelizar:10_insert_responded_applicants_values

insert into RESPONDED_APPLICANTS (RESUME_ID, VACANCY_ID, CONFIRMATION)
values ((select id from RESUMES where name like 'SMM-менеджер'), (select id from VACANCIES where name like 'SMM-специалист'), true),
       ((select id from RESUMES where name like 'Java-разработчик'), (select id from VACANCIES where name like 'Java-разработчик'), true);