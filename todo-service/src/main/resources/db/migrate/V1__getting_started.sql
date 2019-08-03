CREATE TABLE todo
(
  id           BIGSERIAL,
  user_id      TEXT    NOT NULL,
  todo_text    TEXT    NOT NULL,
  is_completed BOOLEAN NOT NULL,
  created_at   TIMESTAMP WITH TIME ZONE,
  updated_at   TIMESTAMP WITH TIME ZONE
);
