CREATE TABLE IF NOT EXISTS authorities(
    email varchar(50) not null primary key,
    authority varchar(50) not null,
    foreign key (email) references users(email) on update cascade
);

--roles are one of these ROLE_FREE, ROLE_PAID, ROLE_ADMIN

CREATE UNIQUE INDEX email_role ON authorities (email,authority);