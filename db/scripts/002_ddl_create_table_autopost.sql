create table auto_post
(
    id   serial primary key,
    description varchar not null,
    created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    auto_user_id int references auto_user(id)
);