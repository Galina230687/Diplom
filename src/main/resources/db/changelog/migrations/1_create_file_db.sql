    -- changeset developer: 1
CREATE TABLE files
(
    id           SERIAL PRIMARY KEY,
    hash         VARCHAR(255)             NOT NULL,
    file_name    VARCHAR(255)             NOT NULL,
    type         VARCHAR(255)             NOT NULL,
    size         BIGINT                   NOT NULL,
    file_byte    OID                    NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_date TIMESTAMP WITH TIME ZONE,
    is_delete    BOOLEAN                           DEFAULT false,
    user_id      BIGINT REFERENCES  files(id)
);
