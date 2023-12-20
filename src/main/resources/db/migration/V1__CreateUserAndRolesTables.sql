CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(255),
    user_passwd VARCHAR(255),
    user_email VARCHAR(255)
);

CREATE TABLE roles (
    user_id INTEGER REFERENCES users(id),
    user_role VARCHAR(255),
    PRIMARY KEY (user_id, user_role)
);