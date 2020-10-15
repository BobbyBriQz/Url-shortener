CREATE TABLE IF NOT EXISTS users(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name varchar(50) null,
    last_name varchar(50) null,
    email varchar(50) not null,
    password varchar(50) not null,
    created_at timestamp default current_timestamp,
    verified_at timestamp null,
    is_active int default 0,
    payment_due_at timestamp null,
    last_payment timestamp null,
    current_account_tier enum('free','monthly','quarterly','biannually','yearly', 'one-time'),
    updated_at timestamp default null on update current_timestamp
);