create table history
(
    id          SERIAL PRIMARY KEY,
    owner_id    int not null,
    startAt     timestamp,
    endAt       timestamp,
    car_id      int REFERENCES car(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);