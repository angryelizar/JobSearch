--liquibase formatted sql
--changeset angryelizar:03_insert_categories_values

insert into CATEGORIES (NAME)
values ('Маркетинг'),
       ('IT');