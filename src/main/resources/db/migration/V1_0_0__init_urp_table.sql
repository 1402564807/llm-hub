CREATE SCHEMA IF NOT EXISTS llm_hub;

CREATE TABLE llm_hub."user"
(
    "id"            BIGINT                                           NOT NULL UNIQUE,
    "username"      VARCHAR                                          NOT NULL UNIQUE,
    "password"      VARCHAR                                          NOT NULL,
    "enable"        BOOLEAN                                          NOT NULL DEFAULT TRUE,
    "nickname"      VARCHAR(32)                                      NOT NULL,
    "email"         VARCHAR(64),
    "all_quota"     NUMERIC     DEFAULT 0                            NOT NULL,
    "used_quota"    NUMERIC     DEFAULT 0                            NOT NULL,
    "request_count" INTEGER     DEFAULT 0                            NOT NULL,
    "group"         VARCHAR(32) DEFAULT 'default'::CHARACTER VARYING NOT NULL,
    "created_time"  TIMESTAMP   DEFAULT NOW()                        NOT NULL,
    "deleted_time"  TIMESTAMP,
    "updated_time"  TIMESTAMP,
    PRIMARY KEY ("id")
);

CREATE TABLE llm_hub."permission"
(
    "id"   BIGINT  NOT NULL UNIQUE,
    "code" VARCHAR NOT NULL,
    "name" VARCHAR NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE llm_hub."role"
(
    "id"   BIGINT  NOT NULL UNIQUE,
    "code" VARCHAR NOT NULL,
    "name" VARCHAR NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE llm_hub."role_permission_map"
(
    "role_id"       BIGINT NOT NULL,
    "permission_id" BIGINT NOT NULL,
    PRIMARY KEY ("role_id", "permission_id")
);

CREATE TABLE llm_hub."user_role_map"
(
    "user_id" BIGINT NOT NULL,
    "role_id" BIGINT NOT NULL,
    PRIMARY KEY ("user_id", "role_id")
);


ALTER TABLE "llm_hub"."role_permission_map"
    ADD FOREIGN KEY ("role_id") REFERENCES "llm_hub"."role" ("id") ON DELETE CASCADE;
ALTER TABLE "llm_hub"."role_permission_map"
    ADD FOREIGN KEY ("permission_id") REFERENCES "llm_hub"."permission" ("id") ON DELETE CASCADE;
ALTER TABLE "llm_hub"."user_role_map"
    ADD FOREIGN KEY ("role_id") REFERENCES "llm_hub"."role" ("id") ON DELETE CASCADE;
ALTER TABLE "llm_hub"."user_role_map"
    ADD FOREIGN KEY ("user_id") REFERENCES "llm_hub"."user" ("id") ON DELETE CASCADE;
