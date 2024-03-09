create table if not exists users (
    id long primary key auto_increment not null,
    name text not null,
    surname text not null,
    email text unique not null,
    phone_number varchar(55) unique,
    password text not null,
    avatar text,
    age integer,
    account_type varchar(50) not null,
);