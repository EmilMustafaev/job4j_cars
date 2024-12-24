create table history(
    id serial primary key,
    startAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);