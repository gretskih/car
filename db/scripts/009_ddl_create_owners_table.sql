CREATE TABLE owner
(
    id       SERIAL PRIMARY KEY,
    name     varchar not null,
    history_id int not null unique references history(id)
);