--liquibase formatted sql
--changeset angryelizar:18-drop_old_data

delete from RESUMES;

delete from VACANCIES;

delete from RESPONDED_APPLICANTS;

delete from MESSAGES;

delete from CONTACTS_INFO;

delete from EDUCATION_INFO;

delete from users;