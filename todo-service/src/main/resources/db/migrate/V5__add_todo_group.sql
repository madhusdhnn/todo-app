CREATE TABLE todo_group
(
  id         BIGSERIAL PRIMARY KEY,
  user_id    TEXT NOT NULL,
  group_name TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE,
  updated_at TIMESTAMP WITH TIME ZONE,
  CONSTRAINT todo_group_uk UNIQUE (user_id, group_name)
);

ALTER TABLE todo
  ADD COLUMN group_id BIGINT;

ALTER TABLE todo
  ADD CONSTRAINT todo_fk FOREIGN KEY (group_id) REFERENCES todo_group (id);