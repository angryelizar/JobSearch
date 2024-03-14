insert into CONTACTS_INFO (TYPE_ID, RESUME_ID, content)
values ((select id from CONTACT_TYPES where TYPE = 'Telegram'), (select id from RESUMES where name like 'SMM-менеджер'), 't.me/angryelizar'),
       ((select id from CONTACT_TYPES where TYPE = 'Телефон'), (select id from RESUMES where name like 'Java-разработчик'), '+996708801562');