--liquibase formatted sql
--changeset angryelizar:13_change_avatar_column_for_users

ALTER TABLE USERS ALTER COLUMN avatar VARCHAR(255);
