CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,  -- The unique identifier for login
                       password VARCHAR(255) NOT NULL,      -- User's password
                       first_name VARCHAR(100) NOT NULL,    -- User's name
                       last_name VARCHAR(100) NOT NULL,     -- User's surname
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_user_email ON users (email);