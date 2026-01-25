ALTER TABLE users DROP COLUMN IF EXISTS is_deleted;
ALTER TABLE board DROP COLUMN IF EXISTS is_deleted;
ALTER TABLE todo_item DROP COLUMN IF EXISTS is_deleted;
ALTER TABLE password_reset_tokens DROP COLUMN IF EXISTS is_deleted;