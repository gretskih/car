CREATE TABLE car
(
    id              SERIAL PRIMARY KEY,
    name            varchar not null,
    engine_id       int not null references engine(id),
    owner_id        int not null unique references owner(id)
);