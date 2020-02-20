create table user
(
    id                 int primary key AUTO_INCREMENT,
    username           varchar(10) unique,
    encrypted_password varchar(100),
    avatar             varchar(100),
    created_at         datetime,
    updated_at         datetime
);

