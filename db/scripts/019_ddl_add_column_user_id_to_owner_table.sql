alter table owner
add column user_id int not null unique references auto_user(id);