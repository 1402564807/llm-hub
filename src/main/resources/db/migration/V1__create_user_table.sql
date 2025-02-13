-- auto-generated definition
create table "user"
(
    id            varchar(32)                                      not null
        constraint user_pk
            primary key,
    username      varchar(32)                                      not null,
    password      varchar(32),
    nickname      varchar(32)                                      not null,
    role          varchar(8)[]                                     not null,
    email         varchar(64),
    all_quota     numeric     default 0                            not null,
    used_quota    numeric     default 0                            not null,
    request_count integer     default 0                            not null,
    "group"       varchar(32) default 'default'::character varying not null,
    created_time  timestamp   default now()                        not null,
    deleted_time  timestamp,
    updated_time  timestamp
);

comment on column "user".all_quota is '所有配额';

comment on column "user".used_quota is '已用配额';

comment on column "user".request_count is '请求次数';

alter table "user"
    owner to postgres;

create unique index user_username_uindex
    on "user" (username);

