create table if not exists users
(
    id           long primary key auto_increment not null,
    name         text                            not null,
    surname      text                            not null,
    email        text unique                     not null,
    phone_number varchar(55) unique,
    password     text                            not null,
    avatar       text,
    age          integer,
    account_type varchar(50)                     not null
);

create table if not exists contact_types
(
    id   long primary key auto_increment not null,
    type text                            not null unique
);

create table if not exists categories
(
    id        long primary key auto_increment not null,
    parent_id long,
    name      text                            not null
);

create table if not exists vacancies
(
    id           long primary key auto_increment not null,
    name         text                            not null,
    description  text                            not null,
    category_id  long                            not null,
    salary       double                          not null,
    exp_from     integer,
    exp_to       integer,
    is_Active    boolean                         not null,
    author_id    long                            not null,
    created_date datetime                        not null,
    update_time  datetime                        not null
);

