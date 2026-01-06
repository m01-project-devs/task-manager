ALTER TABLE users
    ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'USER';

INSERT INTO users (email, password, first_name, last_name, role)
VALUES (
  'admin@example.com',
  '$2a$10$eMe.xeMs78Z2gr9sKBe5PeDaoE7buLYI.2dGyuFIYiNz8SqWXIK1u', -- "admin"
  'System',
  'Admin',
  'ADMIN'
);