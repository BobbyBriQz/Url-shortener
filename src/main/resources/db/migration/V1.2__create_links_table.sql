CREATE TABLE IF NOT EXISTS links(
    id int not null auto_increment primary key,
    slug varchar(50) not null unique,
    url varchar(50) not null,
    visit_count bigint default 0,
    created_at timestamp default current_timestamp,
    updated_at timestamp null on update current_timestamp,
    user_id int,
    foreign key (user_id) references users(id) on delete cascade
);