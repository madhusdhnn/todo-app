CREATE TABLE auth_info
(
  id          BIGSERIAL PRIMARY KEY,
  user_id     TEXT                     NOT NULL UNIQUE,
  auth_token  TEXT                     NOT NULL,
  expiry_time TIMESTAMP WITH TIME ZONE NOT NULL
);