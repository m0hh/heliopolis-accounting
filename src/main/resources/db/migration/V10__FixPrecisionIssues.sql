ALTER TABLE pay
ALTER COLUMN total_hours  TYPE decimal(10,2),
ALTER COLUMN total_pay TYPE  decimal(10,2),
ALTER COLUMN total_deduction  TYPE decimal(10,2);

ALTER TABLE users
ALTER COLUMN hourly_rate TYPE decimal(10,2);