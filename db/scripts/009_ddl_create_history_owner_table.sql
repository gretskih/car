create table history_owner
(
    id          SERIAL PRIMARY KEY,
    car_id      int not null references car(id),
    owner_id    int not null references owner(id)
);