CREATE TABLE todo_item (
    id BIGSERIAL PRIMARY KEY,
    board_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_todo_item_board
        FOREIGN KEY (board_id)
        REFERENCES board(id)
);

CREATE INDEX idx_todo_item_board_id ON todo_item(board_id);
