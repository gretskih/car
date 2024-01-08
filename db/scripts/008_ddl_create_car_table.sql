CREATE TABLE car
(
    id              SERIAL PRIMARY KEY,
    name            varchar not null,
    mileage         int     not null,
    year_prod       int     not null,
    bodyType        varchar not null,
    gearbox         varchar not null,
    fuelType        varchar not null,
    engine_id       int     not null references engine(id),
    owner_id        int     not null unique references owner(id)
);