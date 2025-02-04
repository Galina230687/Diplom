    -- changeset developer: 2
CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    login        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    role         VARCHAR(255),
    created_date TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
