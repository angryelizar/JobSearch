--liquibase formatted sql
--changeset angryelizar:02_insert_users_values

insert into users (name, surname, email, password, account_type)
values ('Елизар', 'Коновалов', 'elizar_01@inbox.ru', 'qwerty', 'Соискатель'),
       ('Садыр', 'Жапаров', 'japarov@inbox.ru', 'qwerty', 'Работодатель');