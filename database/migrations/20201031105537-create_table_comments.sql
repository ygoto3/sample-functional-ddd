
-- +migrate Up
CREATE TABLE IF NOT EXISTS comments (
    id VARCHAR(36) NOT NULL,
    body VARCHAR(255) NOT NULL,
    PRIMARY KEY(id)
);

-- +migrate Down
DROP TABLE IF EXISTS comments;
