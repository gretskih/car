CREATE TABLE photo
(
    id       SERIAL PRIMARY KEY,
    name     varchar not null,
    path     varchar not null,
    auto_id int REFERENCES car(id)
);