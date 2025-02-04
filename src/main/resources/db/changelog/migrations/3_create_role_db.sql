    -- changeset developer: 3
CREATE TABLE user_roles
(
    user_id BIGINT REFERENCES users (id),
    roles   VARCHAR(15) DEFAULT 'ROLE_USER'
);
