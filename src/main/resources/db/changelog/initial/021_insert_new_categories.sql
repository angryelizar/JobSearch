--liquibase formatted sql
--changeset angryelizar:21-insert_new_categories

insert into CATEGORIES(NAME)
VALUES ('Автомобильный бизнес'), ('Искусство, развлечения, массмедиа'), ('Медицина, фармацевтика'), ('Продажи, обслуживание клиентов');