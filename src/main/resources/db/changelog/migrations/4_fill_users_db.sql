-- changeset developer: 4
INSERT INTO users (login, password)
VALUES ('user1@test.ru', '$2a$12$atmCmIvm5EeC2mdBiXDVI.7M1UfxEWPlfR20Zy/roSTPG8pbGKc4i'),
       ('admin1@test.ru', '$2a$12$yM.Hwf8nbGB3HsAd7v9Y5eJhbihEk0qFchU2WveRkq1X31SOHO42u');

INSERT INTO user_roles(user_id, roles)
VALUES (1, 'ROLE_USER'),
       (2, 'ROLE_ADMIN')