CREATE TABLE auth_info_archive
(
  id         BIGSERIAL PRIMARY KEY,
  user_id    TEXT                     NOT NULL UNIQUE,
  auth_token TEXT                     NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);