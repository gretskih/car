alter table car
add column brand_id int not null references brand(id);
