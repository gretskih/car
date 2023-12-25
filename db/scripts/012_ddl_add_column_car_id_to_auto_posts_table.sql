alter table auto_post
add column car_id int not null unique references car(id);