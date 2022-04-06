drop table if exists plan;
create table plan (
    id              serial primary key,
    name            varchar(80) NOT NULL,
    description     varchar(256)
);

drop table if exists users;
create table users (
    id          serial primary key,
    name        varchar(80) NOT NULL,
    login       varchar(40) NOT NULL,
    password    varchar(40) NOT NULL,
    phone       varchar(40),
    email       varchar(40)
);

drop table if exists account;
create table account (
    account_id  serial primary key,
    user_id     integer,
    plan        integer,
    money       bigint NOT NULL,
    foreign key (plan) references plan (id),
    foreign key (user_id) references users (id)
);

drop table if exists card;
create table card (
    id              serial primary key,
    name            varchar(80) NOT NULL,
    type            varchar(80) NOT NULL,
    account_id      integer,
    foreign key (account_id) references account (account_id)
);

insert into plan values (0, 'Simple', '6% per year');
insert into plan values (1, 'VIP', '256% per year');

insert into users values (0, 'Anna A.', 'Anna', '12345678', '2224412', NULL);
insert into users values (1, 'Nick K.', 'Nick', '12345678', '3132131', NULL);
insert into users values (2, 'Santa C.', 'Santa', '12345678', '3213123', NULL);

insert into account values(0, 0, 0, 1000);
insert into account values(1, 0, 1, 100000);
insert into account values(2, 1, 0, 0);
insert into account values(3, 2, 0, 1);

insert into card values (0, '1232 2428 1232 2428', 'Credit card', 0);
insert into card values (1, '1232 2428 3134 1314', 'Credit card', 1);
insert into card values (2, '1232 2428 3134 3131', 'Credit card', 1);
insert into card values (3, '1232 2428 3134 3245', 'Credit card', 2);
insert into card values (4, '1232 2428 3134 7474', 'Credit card', 3);

