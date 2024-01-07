alter table owner
add column auto_id int REFERENCES car(id)
ON DELETE CASCADE
ON UPDATE CASCADE