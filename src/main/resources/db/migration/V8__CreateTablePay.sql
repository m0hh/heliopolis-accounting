CREATE TABLE pay (
    id SERIAL PRIMARY KEY,
    total_hours decimal(5,5) NOT NULL,
    total_pay decimal(5,5) NOT NULL,
    total_deduction decimal(5,5) NULL NULL,
    user_id INTEGER NOT NULL,
    created_at DATE NOT NULL,
    CONSTRAINT fk_pay_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT unique_date_user UNIQUE (user_id, created_at)
);

ALTER TABLE shifts
ADD COLUMN pay_id INTEGER;

ALTER TABLE shifts
ADD CONSTRAINT fk_pay_id FOREIGN KEY (pay_id) REFERENCES pay(id);
