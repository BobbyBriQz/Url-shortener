package cc.lnkd.urlshortener.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegisteredUser {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String createdAt;
    private String verifiedAt;
    private boolean isEnabled;


    /*
    * id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name varchar(50) null,
    last_name varchar(50) null,
    email varchar(50) not null unique,
    password varchar(50) not null,
    created_at timestamp default current_timestamp,
    verified_at timestamp null,
    enabled boolean default false,
    sub_is_active boolean default false,
    payment_due_at timestamp null,
    last_payment timestamp null,
    current_account_tier enum('free','monthly','quarterly','biannually','yearly', 'one-time') default 'free',
    updated_at timestamp default null on update current_timestamp
    * */
}
