CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    amount decimal(10,2) NOT NULL,
    created_at timestamp without time zone NOT NULL,
);