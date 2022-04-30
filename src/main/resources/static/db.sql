create table wallets
(
    id       serial
        primary key,
    name     varchar(100)                                 not null,
    currency varchar(10) default 'USD'::character varying not null
);

alter table wallets
    owner to postgres;

create table groups
(
    id   serial
        constraint groups_pk
            primary key,
    type varchar(50) not null
);

alter table groups
    owner to postgres;

create table categories
(
    id      serial
        primary key,
    name    varchar(50) not null,
    icon    varchar(50) not null,
    idgroup integer     not null
        references groups
);

alter table categories
    owner to postgres;

create table transactions
(
    id          serial
        primary key,
    date        timestamp      not null,
    description varchar(300),
    photo       varchar(50),
    value       numeric(10, 2) not null,
    idwallet    integer        not null
        references wallets,
    idcategory  integer        not null
        references categories
);

alter table transactions
    owner to postgres;

create unique index groups_id_uindex
    on groups (id);

