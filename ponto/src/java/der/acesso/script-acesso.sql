--==============================================================--
--                                INICIO                        --
--==============================================================--
CREATE SCHEMA "security" AUTHORIZATION "postgres";
SET search_path = security,pg_catalog;

drop table if EXISTS "security"."roles_x_groups";
drop table if EXISTS "security"."users_x_roles";
drop table if EXISTS "security"."users";
drop table if EXISTS "security"."roles";

CREATE TABLE "security"."users" (
  "matricula" VARCHAR(14) NOT NULL, 
  "id_situacao" BOOLEAN, 
  "nome_referencia" VARCHAR(20), 
  "nome_completo" VARCHAR(50), 
  "senha" VARCHAR(32),
  CONSTRAINT "users_pk" PRIMARY KEY("matricula")
) WITH OIDS;

CREATE TABLE "security"."roles" (
  "id_role" INTEGER NOT NULL, 
  "conditional" BOOLEAN NOT NULL, 
  "role_name" VARCHAR(100), 
  CONSTRAINT "roles_pk" PRIMARY KEY("id_role")
) WITH OIDS;

CREATE TABLE "security"."roles_x_groups" (
  "id_role" INTEGER NOT NULL, 
  "id_group" INTEGER NOT NULL, 
  CONSTRAINT "roles_x_groups_pk" PRIMARY KEY("id_role", "id_group"), 
  CONSTRAINT "roles_fk_groups" FOREIGN KEY ("id_group")
    REFERENCES "security"."roles"("id_role")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE, 
  CONSTRAINT "roles_fk_roles" FOREIGN KEY ("id_role")
    REFERENCES "security"."roles"("id_role")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE
) WITH OIDS;

CREATE TABLE "security"."users_x_roles" (
  "id_user" VARCHAR(14) NOT NULL, 
  "id_role" INTEGER NOT NULL, 
  CONSTRAINT "users_x_roles_pk" PRIMARY KEY("id_user", "id_role"), 
  CONSTRAINT "users_x_roles_fk_roles" FOREIGN KEY ("id_role")
    REFERENCES "security"."roles"("id_role")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE, 
  CONSTRAINT "users_x_roles_fk_users" FOREIGN KEY ("id_user")
    REFERENCES "security"."users"("matricula")
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    NOT DEFERRABLE
) WITH OIDS;

--comentario -> a senha criptografada é admin
INSERT INTO SECURITY.USERS VALUES ('admin',true,'Administrador','Administrador','21232f297a57a5a743894a0e4a801fc3');
INSERT INTO SECURITY.ROLES VALUES (1,true,'ADMIN');
INSERT INTO SECURITY.ROLES VALUES (2,true,'RH');
INSERT INTO SECURITY.USERS_X_ROLES VALUES ('admin',1);
--==============================================================--
--                                FIM                           --
--==============================================================--
