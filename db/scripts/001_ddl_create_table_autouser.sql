create table auto_user
(
    id   serial primary key,
    login varchar not null,
    password varchar not null
);