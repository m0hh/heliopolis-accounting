CREATE TABLE shifts (
    id SERIAL PRIMARY KEY,
    user_id_opened INTEGER NOT NULL,
    user_id_closed INTEGER,
    total_orders decimal(10,2),
    total_credits decimal(10,2),
    total_expenses decimal(10,2),
    total_shift decimal(10,2),
    created_at timestamp without time zone NOT NULL,
    closed_at timestamp without time zone,
    CONSTRAINT fk_shift_user_open FOREIGN KEY (user_id_opened) REFERENCES users(id),
    CONSTRAINT fk_shift_user_close FOREIGN KEY (user_id_closed) REFERENCES users(id)
);

ALTER TABLE orders ADD COLUMN shift_id INTEGER NOT NULL;
ALTER TABLE orders ADD CONSTRAINT shift_id_order_fk FOREIGN KEY (shift_id) REFERENCES shifts(id);

ALTER TABLE expenses ADD COLUMN shift_id INTEGER NOT NULL;
ALTER TABLE expenses ADD CONSTRAINT shift_id_expense_fk FOREIGN KEY (shift_id) REFERENCES shifts(id);

ALTER TABLE credits ADD COLUMN shift_id INTEGER NOT NULL;
ALTER TABLE credits ADD CONSTRAINT shift_id_credit_fk FOREIGN KEY (shift_id) REFERENCES shifts(id);

