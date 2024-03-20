--liquibase formatted sql
--changeset angryelizar:08_insert_education_info_values

insert into EDUCATION_INFO (RESUME_ID, INSTITUTION, PROGRAM, START_DATE, END_DATE, DEGREE)
values ((select id from RESUMES where name like 'SMM-менеджер'), 'Bold Brands Education', 'SMM за полгода', '2015-03-01', '2015-06-01',
        'SMM-менеджер'),
       ((select id from RESUMES where name like 'Java-разработчик'), 'Attractor School', 'Java за полгода', '2016-03-01', '2016-06-01',
        'Junior Java-developer');