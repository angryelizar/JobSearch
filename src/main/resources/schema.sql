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
    name      text                            not null,
    FOREIGN KEY (parent_id) references categories (id)
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
    is_active    boolean                         not null,
    author_id    long                            not null,
    created_date datetime                        not null,
    update_time  datetime                        not null,
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (author_id) references users (id)
);

create table if not exists resumes
(
    id           long primary key auto_increment not null,
    applicant_id long                            not null,
    name         text                            not null,
    category_id  long                            not null,
    salary       double                          not null,
    is_active    boolean                         not null,
    created_date datetime                        not null,
    update_time  datetime                        not null,
    FOREIGN KEY (applicant_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

create table if not exists contacts_info
(
    id        long primary key auto_increment not null,
    type_id   long                            not null,
    resume_id long                            not null,
    "value"     text                            not null,
    FOREIGN KEY (type_id) REFERENCES contact_types (id),
    FOREIGN KEY (resume_id) REFERENCES resumes (id)
);

create table if not exists education_info
(
    id          long primary key auto_increment not null,
    resume_id   long                            not null,
    institution text                            not null,
    program     text                            not null,
    start_date  date                            not null,
    end_date    date,
    degree      text                            not null,
    FOREIGN KEY (resume_id) REFERENCES resumes (id)
);

create table if not exists work_experience_info
(
    id               long primary key auto_increment not null,
    resume_id        long                            not null,
    years            integer                         not null,
    company_name     text                            not null,
    position         text                            not null,
    responsibilities text                            not null,
    FOREIGN KEY (resume_id) REFERENCES resumes (id)
);

create table if not exists responded_applicants
(
    id           long primary key auto_increment not null,
    resume_id    long                            not null,
    vacancy_id   long                            not null,
    confirmation boolean                         not null,
    FOREIGN KEY (resume_id) REFERENCES resumes (id),
    FOREIGN KEY (vacancy_id) REFERENCES vacancies (id)
);

create table if not exists messages
(
    id                   long primary key auto_increment not null,
    responded_applicants long                            not null,
    content              text                            not null,
    "date_time"             datetime                        not null,
    FOREIGN KEY (responded_applicants) REFERENCES responded_applicants (id)
);




