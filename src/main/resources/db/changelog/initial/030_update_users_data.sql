--liquibase formatted sql
--changeset angryelizar:30-update-users-data

update USERS
SET AUTHORITY_ID = (SELECT ID FROM AUTHORITIES WHERE AUTHORITY = 'APPLICANT')
where EMAIL = 'conovalov.elizar@gmail.com';

update USERS
SET AUTHORITY_ID = (SELECT ID FROM AUTHORITIES WHERE AUTHORITY = 'EMPLOYER')
where EMAIL = 'attractor@attractor.kg';

update USERS
SET AUTHORITY_ID = (SELECT ID FROM AUTHORITIES WHERE AUTHORITY = 'ADMIN')
where EMAIL = 'example@example.com';
