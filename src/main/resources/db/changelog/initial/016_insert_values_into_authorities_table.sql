--liquibase formatted sql
--changeset angryelizar:16-insert_values_into_authorities_table

insert into authorities (authority)
values ( 'APPLICANT' ), ('EMPLOYER'), ('ADMIN');