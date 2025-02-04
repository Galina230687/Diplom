create table if not exists PERSONS (
ID              BIGSERIAL PRIMARY KEY,
name            TEXT not null,
surname         TEXT not null,
age             INTEGER not null,
phone_number    TEXT,
city_of_living  TEXT
);



