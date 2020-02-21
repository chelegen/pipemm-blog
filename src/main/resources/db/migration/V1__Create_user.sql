create table user
(
    id                 bigint primary key AUTO_INCREMENT,
    username           varchar(10) unique,
    encrypted_password varchar(100),
    avatar             varchar(100) default 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1651318081,2860235060&fm=26&gp=0.jpg',
    created_at         datetime,
    updated_at         datetime
);

