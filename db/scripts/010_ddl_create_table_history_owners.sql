create table history_owners(
    id serial primary key,
    owner_id int not null references owners(id),
    car_id int not null references car(id),
    unique(owner_id, car_id)
);