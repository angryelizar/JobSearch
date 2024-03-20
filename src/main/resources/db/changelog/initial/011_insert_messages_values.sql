--liquibase formatted sql
--changeset angryelizar:11_insert_messages_values

insert into MESSAGES (RESPONDED_APPLICANTS, CONTENT, date_time)
values ((select id from RESPONDED_APPLICANTS where id = 1), 'Салам молекулам!', '2024-03-10 16:20:00');