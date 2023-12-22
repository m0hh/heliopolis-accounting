CREATE TABLE expenses (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    amount decimal(10,2) NOT NULL,
    description TEXT,
    createdAt timestamp without time zone NOT NULL,
    CONSTRAINT fk_expense_user FOREIGN KEY (user_id) REFERENCES users(id)
);