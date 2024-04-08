--liquibase formatted sql
--changeset angryelizar:27-default-value-for-avatar

ALTER TABLE USERS ALTER COLUMN avatar SET DEFAULT 'default_avatar.jpeg';
UPDATE USERS SET AVATAR = 'default_avatar.jpeg' WHERE AVATAR IS NULL;