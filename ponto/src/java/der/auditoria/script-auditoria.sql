--==============================================================--
--                                INICIO                        --
--==============================================================--
CREATE SCHEMA auditoria AUTHORIZATION postgres;
SET search_path = auditoria, pg_catalog;

DROP SEQUENCE IF EXISTS auditoria.auditoria_id_seq;
DROP TABLE IF EXISTS auditoria.auditoria;
DROP TABLE if exists auditoria.log_acesso;

--
-- Structure for table auditoria (OID = 34554) : 
--
CREATE TABLE auditoria.auditoria (
    id bigint NOT NULL,
    data_transacao date,
    entidade varchar(255),
    hora_transacao time without time zone,
    matricula varchar(15),
    tipo_transacao varchar(30),
    transacao varchar(255)
) WITHOUT OIDS;
--
-- Definition for sequence auditoria_id_seq (OID = 34562) : 
--
CREATE SEQUENCE auditoria.auditoria_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
--
-- Definition for index auditoria_pkey (OID = 34560) : 
--
ALTER TABLE ONLY auditoria
    ADD CONSTRAINT auditoria_pkey PRIMARY KEY (id);

CREATE TABLE auditoria.log_acesso
(
  id bigint NOT NULL,
  data_acesso date NOT NULL,
  hora_acesso timestamp without time zone,
  hora_logout timestamp without time zone,
  matricula character varying(20) NOT NULL,
  CONSTRAINT log_acesso_pkey PRIMARY KEY (id)
) WITHOUT OIDS;

ALTER TABLE auditoria.log_acesso OWNER TO postgres;

-- DROP SEQUENCE auditoria.log_acesso_id_seq;
CREATE SEQUENCE auditoria.log_acesso_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

ALTER TABLE auditoria.log_acesso_id_seq OWNER TO postgres;
--==============================================================--
--                                FIM                           --
--==============================================================--
