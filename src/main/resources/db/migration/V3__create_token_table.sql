-- auto-generated definition
create table token
(
    id              varchar(32)             not null
        constraint token_pk
            primary key,
    user_id         varchar(32)             not null,
    name            varchar(128)            not null,
    key             varchar(128)            not null,
    subnet          varchar(32)[]           not null,
    models          varchar(32)[]           not null,
    used_quota      numeric   default 0     not null,
    all_quota       numeric   default 0     not null,
    unlimited_quota boolean   default true  not null,
    created_time    timestamp default now() not null,
    accessed_time   timestamp,
    expired_time    timestamp,
    status          boolean   default true  not null
);

comment on column token.subnet is '访问白名单';

comment on column token.models is '可用模型';

comment on column token.used_quota is '已使用配额';

comment on column token.all_quota is '配额总量';

comment on column token.unlimited_quota is '是否无限配额';

comment on column token.expired_time is '到期时间';

alter table token
    owner to postgres;

create unique index token_key_uindex
    on token (key);

