INSERT INTO llm_hub."user" ("id", "username", "password", "nickname", "email", "group")
VALUES (1, 'admin', '$2a$10$7zfEdqQYJrBnmDdu7UkgS.zOAsJf4bB1ZYrVhCBAIvIoPbEmeVnVe', 'admin', 'admin@llmhub.com', 'default');


INSERT INTO "llm_hub"."role" ("id", "code", "name")
VALUES (1, 'ADMIN', 'ADMIN'),
       (2, 'GENERAL', 'GENERAL');

INSERT INTO "llm_hub"."permission" ("id", "code", "name")
VALUES (1, 'WRITE_USER_ROLE_PERMISSION', 'WRITE_USER_ROLE_PERMISSION'),
       (2, 'READ_USER_ROLE_PERMISSION', 'READ_USER_ROLE_PERMISSION');

INSERT INTO "llm_hub"."user_role_map" ("user_id", "role_id")
VALUES (1, 1);

INSERT INTO "llm_hub"."role_permission_map" ("role_id", "permission_id")
VALUES (1, 1),
       (1, 2);
