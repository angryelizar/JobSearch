--liquibase formatted sql
--changeset angryelizar:34-update-users-data

update USERS
SET preferred_locale = 'ru_KG'
where EMAIL = 'conovalov.elizar@gmail.com';

update USERS
SET preferred_locale = 'ky_KG'
where EMAIL = 'attractor@attractor.kg';

update USERS
SET preferred_locale = null
where EMAIL = 'example@example.com';