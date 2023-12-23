CREATE TABLE credits (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    amount decimal(10,2) NOT NULL,
    description TEXT,
    created_at timestamp without time zone NOT NULL,
    CONSTRAINT fk_credit_user FOREIGN KEY (user_id) REFERENCES users(id)
);