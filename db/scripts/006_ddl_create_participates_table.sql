CREATE TABLE participates (
   id serial PRIMARY KEY,
   auto_user_id int not null REFERENCES auto_user(id),
   auto_post_id int not null REFERENCES auto_post(id),
   UNIQUE (auto_user_id, auto_post_id)
);