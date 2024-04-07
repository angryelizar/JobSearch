--liquibase formatted sql
--changeset angryelizar:05_insert_contact_types_values

insert into CONTACT_TYPES (TYPE)
values ('Телефон'),
       ('Telegram'),
       ('WhatsApp'),
       ('E-mail'),
       ('Linkedin');
