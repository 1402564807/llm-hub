-- auto-generated definition
create table service_provider
(
    id           varchar(32)                                      not null
        constraint service_provider_pk
            primary key,
    type         integer                                          not null,
    name         varchar(32)                                      not null,
    weight       integer     default 1                            not null,
    key          varchar(256)                                     not null,
    base_url     varchar(128)                                     not null,
    models       varchar(32)[]                                    not null,
    "group"      varchar(32) default 'default'::character varying not null,
    used_quota   numeric     default 0                            not null,
    model_map    jsonb                                            not null,
    created_time timestamp   default now()                        not null,
    status       boolean     default true                         not null
);

comment on column service_provider.weight is '权重';

comment on column service_provider.key is '密钥';

comment on column service_provider.models is '可用模型';

comment on column service_provider.used_quota is '已用配额';

alter table service_provider
    owner to postgres;

