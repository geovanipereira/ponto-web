--==============================================================--
--                                INICIO                        --
--==============================================================--
CREATE SCHEMA ponto AUTHORIZATION postgres;
SET search_path = ponto,pg_catalog;

DROP VIEW IF EXISTS "ponto"."vw_frequencias";
DROP VIEW IF EXISTS "ponto"."vw_frequencias_ocor";
DROP VIEW IF EXISTS "ponto"."vw_frequencias_base";
DROP VIEW IF EXISTS "ponto"."vw_funcionarios_escala";
DROP VIEW IF EXISTS "ponto"."vw_grades";
DROP VIEW IF EXISTS "ponto"."vw_ocorrencias";
DROP VIEW IF EXISTS "ponto"."vw_funcionarios_dias";
DROP VIEW IF EXISTS "ponto"."vw_mes_processado";

DROP TABLE IF EXISTS "ponto"."batidas";
DROP TABLE IF EXISTS "ponto"."escalas";
DROP TABLE IF EXISTS "ponto"."feriados";
DROP TABLE IF EXISTS "ponto"."frequencias";
DROP TABLE IF EXISTS "ponto"."historicos";
DROP TABLE IF EXISTS "ponto"."horarios_especiais";
DROP TABLE IF EXISTS "ponto"."mes_processado";
DROP TABLE IF EXISTS "ponto"."ocorrencias";
DROP TABLE IF EXISTS "ponto"."orgaos";
DROP TABLE IF EXISTS "ponto"."funcionarios" CASCADE;
DROP TABLE IF EXISTS "ponto"."lotacoes";
DROP TABLE IF EXISTS "ponto"."grade_horarios";
DROP TABLE IF EXISTS "ponto"."grades";
DROP TABLE IF EXISTS "ponto"."status";

DROP FUNCTION IF EXISTS "ponto"."last_day"(date);
DROP FUNCTION IF EXISTS "ponto"."first_day"(date);
DROP FUNCTION IF EXISTS "ponto"."generate_series"(start_ts timestamp, end_ts timestamp, step interval);
DROP FUNCTION IF EXISTS "ponto"."fun_limpa_mes"();
DROP FUNCTION IF EXISTS "ponto"."fun_processa_mes"();

DROP SEQUENCE IF EXISTS ponto.id_ocorrencias_seq;
DROP LANGUAGE IF EXISTS "plpgsql";

--
-- Definition for language plpgsql (OID = 17571) : 
--
CREATE TRUSTED PROCEDURAL LANGUAGE plpgsql
   HANDLER "plpgsql_call_handler"
   VALIDATOR "pg_catalog"."plpgsql_validator";
   
--==============================================================--
--                            FUNCTIONS                         -- 
--==============================================================--
SET search_path = ponto, pg_catalog;
SET check_function_bodies = false;
   
--
-- Definition for function first_day (OID = 19186) : 
--
--Retorna o primeiro dia do mes da data passada como parametro
--ex: select current_date, FIRST_day(current_date),last_day(current_date)

CREATE OR REPLACE function "ponto"."first_day"(date) returns date as 
  'select cast(date_trunc(''month'', $1) as date)'
language sql;

--
-- Definition for function fun_limpa_mes (OID = 19187) : 
--
CREATE OR REPLACE FUNCTION "ponto"."fun_limpa_mes"() RETURNS trigger AS $$
BEGIN
  DELETE FROM "ponto"."frequencias"
   WHERE dat_freq between "ponto"."first_day"(OLD.dat_mes) and "ponto"."last_day"(OLD.dat_mes);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--
-- Definition for function fun_processa_mes (OID = 19188) : 
--
CREATE OR REPLACE FUNCTION "ponto"."fun_processa_mes"() RETURNS trigger AS $$
BEGIN
  DELETE FROM "ponto"."frequencias"
   WHERE dat_freq between "ponto"."first_day"(NEW.dat_mes) and "ponto"."last_day"(NEW.dat_mes);

  INSERT INTO "ponto"."frequencias"
         (dat_freq, mat_func,log_feriado, num_dia, grade,qt_batidas,
         g_in1, in1, log_in1, 
         g_out1, out1, log_out1, 
         g_in2, in2,  log_in2, 
         g_out2, out2, log_out2 ,
         bco_desc_new, log_13, log_falta)
  SELECT dat_freq, mat_func, log_feriado, num_dia, grade, qt_batidas,
         g_in1, in1, log_in1, 
         g_out1, out1, log_out1, 
         g_in2, in2, log_in2, 
         g_out2, out2, log_out2,
         bco_desc,log_13,log_falta
    FROM "ponto"."vw_frequencias"
   WHERE dat_freq between "ponto"."first_day"(NEW.dat_mes) and "ponto"."last_day"(NEW.dat_mes);

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

--
-- Definition for function generate_series (OID = 19189) : 
--
--exemplo de uso
--SELECT "ponto"."generate_series"( timestamp '1/1/2009', timestamp '1/31/2009', interval '1 day');
CREATE OR REPLACE FUNCTION "ponto"."generate_series"(start_ts timestamp without time zone, end_ts timestamp without time zone, step interval) 
RETURNS SETOF timestamp without time zone 
LANGUAGE sql AS $$
  SELECT
    CASE WHEN $1 < $2 THEN $1
         WHEN $1 > $2 THEN $2
    END + s.i * $3 AS "generate_series"
  FROM generate_series(0, floor(CASE WHEN $1 < $2 AND $3 > INTERVAL '0 seconds' 
                                     THEN extract('epoch' FROM $2) - extract('epoch' FROM $1)
                                     WHEN $1 > $2 AND $3 < INTERVAL '0 seconds' 
                                     THEN extract('epoch' FROM $1) - extract('epoch' FROM $2)
                                 END/extract('epoch' FROM $3))::int8
) AS s(i);
$$;

--
-- Definition for function last_day (OID = 19190) : 
--
--Retorna o ultimo dia do mes da data passada como parametro
--ex: select current_date, last_day(current_date),last_day(current_date)

CREATE OR REPLACE function "ponto"."last_day"(date) returns date as 
  'select cast(date_trunc(''month'', $1) + ''1 month''::interval as date) - 1'
language sql;

-- Function: "isnull"(text, text, text)

-- DROP FUNCTION "isnull"(text, text, text);

CREATE OR REPLACE FUNCTION "ponto"."isnull" (text, text, text) RETURNS text AS $$
BEGIN
  SELECT (CASE (SELECT $1 is null)
          WHEN true THEN $2
          ELSE $3 END) AS RESULT
END $$ LANGUAGE 'sql' VOLATILE CALLED ON NULL INPUT SECURITY INVOKER COST 100;

COMMENT ON FUNCTION "ponto"."isnull"(text, text, text)
IS 'Retorna o 2º arg se o 3º for nulo';

--
-- Structure for table batidas (OID = 19329) : 
--
CREATE TABLE "ponto"."batidas" (
  "data"        DATE NOT NULL, 
  "hora"        TIME WITHOUT TIME ZONE NOT NULL, 
  "num"         INTEGER NOT NULL, 
  "flg_new"     SMALLINT, 
  "num_relogio" smallint
);


--
-- Structure for table escalas (OID = 19332) : 
--
CREATE TABLE "ponto"."escalas" (
  "dat_escala"    DATE NOT NULL, 
  "mat_func"      VARCHAR(14) NOT NULL, 
  "carga_horaria" SMALLINT, 
  "e_in1"         TIME WITHOUT TIME ZONE, 
  "e_in2"         TIME WITHOUT TIME ZONE, 
  "e_out1"        TIME WITHOUT TIME ZONE, 
  "e_out2"        TIME WITHOUT TIME ZONE, 
  "in1_13"        TIME WITHOUT TIME ZONE, 
  "in2_13"        TIME WITHOUT TIME ZONE, 
  "log_12e"       SMALLINT NOT NULL, 
  "out1_13"       TIME WITHOUT TIME ZONE, 
  "out2_13"       TIME WITHOUT TIME ZONE
);
    
    
--
-- Structure for table feriados (OID = 19335) : 
--
CREATE TABLE "ponto"."feriados" (
  "dat_feriado" timestamp without time zone NOT NULL,
  "descricao"   VARCHAR(40) NOT NULL
);


--
-- Structure for table frequencias (OID = 19338) : 
--
CREATE TABLE "ponto"."frequencias" (
  "dat_freq"     DATE NOT NULL, 
  "mat_func"     VARCHAR(14) NOT NULL, 
  "grade"        VARCHAR(10) NOT NULL, --todo
  "bco_desc"     SMALLINT , 
  "bco_desc_new" INTERVAL(0),
  "g_in1"        TIME WITHOUT TIME ZONE, 
  "in1"          TIME WITHOUT TIME ZONE, 
  "log_in1"      VARCHAR(1), 

  "g_out1"       TIME WITHOUT TIME ZONE, 
  "out1"         TIME WITHOUT TIME ZONE, 
  "log_out1"     VARCHAR(1), 

  "g_in2"        TIME WITHOUT TIME ZONE, 
  "in2"          TIME WITHOUT TIME ZONE, 
  "log_in2"      VARCHAR(1), 

  "g_out2"       TIME WITHOUT TIME ZONE, 
  "out2"         TIME WITHOUT TIME ZONE, 
  "log_out2"     VARCHAR(1), 

  "log_13"       SMALLINT NOT NULL, 
  "log_falta"    INTEGER NOT NULL, 
  "log_feriado"  SMALLINT NOT NULL, 
  "num_dia"      SMALLINT NOT NULL, 
  "qt_batidas"   SMALLINT NOT NULL
);

--
-- Structure for table funcionarios (OID = 19344) : 
--
CREATE TABLE "ponto"."funcionarios" (
  "mat_func"          VARCHAR(14) NOT NULL, 
  "carga_horaria"     SMALLINT NOT NULL, 
  "cod_grade"         SMALLINT NOT NULL, 
  "dat_fim"           DATE, 
  "dat_ini"           DATE NOT NULL, 
  "log_registro"      SMALLINT NOT NULL, 
  "mat_func_reduzida" VARCHAR(10) NOT NULL,
  "nom_func"          VARCHAR(50) NOT NULL, 
  "num_func"          SMALLINT NOT NULL, 
  "org_origem"        SMALLINT, 
  "lotacao"           VARCHAR(9) NOT NULL
);

--
-- Structure for table grade_horarios (OID = 19347) : 
--
CREATE TABLE "ponto"."grade_horarios" (
  "cod_grade"  SMALLINT NOT NULL, 
  "num_dia"    SMALLINT NOT NULL,
  "g_in1_min"  TIME WITHOUT TIME ZONE, 
  "g_in1"      TIME WITHOUT TIME ZONE, 
  "g_in1_max"  TIME WITHOUT TIME ZONE, 
  "g_out1_min" TIME WITHOUT TIME ZONE, 
  "g_out1"     TIME WITHOUT TIME ZONE, 
  "g_out1_max" TIME WITHOUT TIME ZONE, 
  "g_in2_min"  TIME WITHOUT TIME ZONE, 
  "g_in2"      TIME WITHOUT TIME ZONE, 
  "g_in2_max"  TIME WITHOUT TIME ZONE, 
  "g_out2_min" TIME WITHOUT TIME ZONE, 
  "g_out2"     TIME WITHOUT TIME ZONE, 
  "g_out2_max" TIME WITHOUT TIME ZONE, 
  "in1_13"     TIME WITHOUT TIME ZONE, 
  "out1_13"    TIME WITHOUT TIME ZONE, 
  "in2_13"     TIME WITHOUT TIME ZONE, 
  "out2_13"    TIME WITHOUT TIME ZONE, 
  "log_12e"    SMALLINT NOT NULL
);

--
-- Structure for table grades (OID = 19350) : 
--
CREATE TABLE "ponto"."grades" (
  "cod_grade" smallint NOT NULL,
  "descricao" VARCHAR(50) NOT NULL
);

--
-- Structure for table historicos (OID = 19353) : 
--
CREATE TABLE "ponto"."historicos" (
  "dat_mes"      DATE NOT NULL, 
  "mat_func"     VARCHAR(14) NOT NULL, 
  "bco_desc"     SMALLINT NOT NULL, 
  "bco_horas"    SMALLINT NOT NULL, 
  "cod_grade"    SMALLINT NOT NULL, 
  "log_atestado" SMALLINT NOT NULL, 
  "qtd_13"       SMALLINT NOT NULL, 
  "qtd_du"       SMALLINT NOT NULL, 
  "qtd_fc"       SMALLINT NOT NULL, 
  "qtd_fe"       SMALLINT NOT NULL, 
  "qtd_fj"       SMALLINT NOT NULL, 
  "qtd_fo"       SMALLINT NOT NULL, 
  "qtd_pc"       SMALLINT NOT NULL, 
  "qtd_pi"       SMALLINT NOT NULL
);

--
-- Structure for table horarios_especiais (OID = 19356) : 
--
CREATE TABLE "ponto"."horarios_especiais" (
  "dat_especial" DATE NOT NULL, 
  "mat_func"     VARCHAR(14) NOT NULL, 
  "cod_grade"    SMALLINT NOT NULL
);

--
-- Definition for sequence id_ocorrencias_seq (OID = 19359) : 
--
CREATE SEQUENCE ponto.id_ocorrencias_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
    
--
-- Structure for table lotacoes (OID = 19361) : 
--
CREATE TABLE "ponto"."lotacoes" (
  "cod_lotacao" VARCHAR(9) NOT NULL, 
  "descricao"   VARCHAR(60) NOT NULL, 
  "sigla"       VARCHAR(10) NOT NULL, 
  "orientador"  VARCHAR(14)
);

--
-- Structure for table mes_processado (OID = 19364) : 
--
CREATE TABLE "ponto"."mes_processado" (
  "dat_mes" timestamp without time zone NOT NULL
);

--
-- Structure for table ocorrencias (OID = 19367) : 
--
CREATE TABLE "ponto"."ocorrencias" (
  id              bigint DEFAULT nextval('id_ocorrencias_seq'::regclass) NOT NULL,
  "mat_func"      VARCHAR(14) NOT NULL, 
  "dat_ocorr"     DATE NOT NULL, 
  "sai_ocorr"     TIME WITHOUT TIME ZONE, 
  "ret_ocorr"     TIME WITHOUT TIME ZONE, 
  "motivo"        VARCHAR(1) NOT NULL, 
  "dsc_ocorr"     TEXT NOT NULL, 
  "dsc_ocorr_rh"  TEXT , 
  "log_bco_horas" SMALLINT,
  "id_status"     SMALLINT DEFAULT 1 NOT NULL
);

--
-- Structure for table orgaos (OID = 19375) : 
--
CREATE TABLE "ponto"."orgaos" (
  "cd_org" SMALLINT NOT NULL, 
  "nm_org" VARCHAR(50) NOT NULL, 
  "sg_org" VARCHAR(10) NOT NULL, 
  "tp_org" VARCHAR(15)
);

--
-- Structure for table status (OID = 28151) : 
--
CREATE TABLE "ponto"."status" (
  "tipo" VARCHAR(40) NOT NULL, 
  "id" SMALLINT NOT NULL,
  "descricao" VARCHAR(40) NOT NULL
) WITHOUT OIDS;

--==============================================================--
--                                VIEWS                         -- 
--==============================================================--
--
-- VIEW vw_mes_processado 
--
create or replace view "ponto"."vw_mes_processado" as
SELECT dat::date, to_char(dat,'D')::int as num_dia , fer.descricao,
       case when fer.dat_feriado is not null then 1 else 0 end as log_feriado
  FROM "ponto"."generate_series"((select "ponto"."first_day"('01/01/2009'::date)), 
                                 (select max(b.data) from ponto.batidas b where b.data < current_date), 
                          interval '1 day') dat
  LEFT JOIN "ponto"."feriados" fer ON fer.dat_feriado = dat;   


--
-- view vw_funcionarios_dias 
--
create or replace view "ponto"."vw_funcionarios_dias" as 
select dias.dat, dias.num_dia, dias.log_feriado,
       fun.mat_func, fun.mat_func_reduzida, fun.nom_func, fun.cod_grade,
       fun.dat_ini, fun.dat_fim, dias.descricao
  from "ponto"."funcionarios" fun, "ponto"."vw_mes_processado" dias 
 where fun.dat_ini <= dias.dat 
   and COALESCE("ponto"."last_day"(fun.dat_fim),"ponto"."last_day"(CURRENT_DATE)) >= dias.dat
   and fun.log_registro = 0;


--
-- view vw_grades
--
CREATE OR REPLACE VIEW "ponto"."vw_grades" AS
SELECT g.cod_grade, g.num_dia, 
       g.g_in1_min,  g.g_in1,  g.g_in1_max, 
       g.g_out1_min, g.g_out1, g.g_out1_max, 
       g.g_in2_min,  g.g_in2,  g.g_in2_max, 
       g.g_out2_min, g.g_out2, g.g_out2_max, 
       g.in1_13, g.out1_13, 
       g.in2_13, g.out2_13, 
       g.log_12e
  FROM "ponto"."grade_horarios" g
 WHERE (g.num_dia <> 9)
 UNION
SELECT g2.cod_grade, seq.num_dia, 
       g2.g_in1_min,  g2.g_in1,  g2.g_in1_max,
       g2.g_out1_min, g2.g_out1, g2.g_out1_max, 
       g2.g_in2_min,  g2.g_in2,  g2.g_in2_max, 
       g2.g_out2_min, g2.g_out2, g2.g_out2_max, 
       g2.in1_13, g2.out1_13, 
       g2.in2_13, g2.out2_13, 
       g2.log_12e
  FROM "ponto"."grade_horarios" g2, (SELECT generate_series(2, 6) AS num_dia) seq
 WHERE (g2.num_dia = 9)
 UNION
SELECT g2.cod_grade, seq.num_dia, 
       null as g_in1_min,  null as g_in1,  null as g_in1_max,
       null as g_out1_min, null as g_out1, null as g_out1_max, 
       null as g_in2_min,  null as g_in2,  null as g_in2_max, 
       null as g_out2_min, null as g_out2, null as g_out2_max, 
       null as in1_13, null as out1_13, 
       null as in2_13, null as out2_13, 
       0 as log_12e
  FROM "ponto"."grade_horarios" g2, (SELECT generate_series(1, 7,6) AS num_dia) seq
 WHERE (g2.num_dia = 9);

--
-- view vw_funcionarios_escala 
--
CREATE OR REPLACE VIEW "ponto"."vw_funcionarios_escala" AS
 SELECT fre.dat as dat_freq, fre.num_dia, 
        fre.mat_func,fre.mat_func_reduzida, fre.nom_func,  
  case when esc.dat_escala is not null then 'ESCALA' 
       when esp.cod_grade is not null then 'E-'||(esp.cod_grade::text)
       else 'G-'||(fre.cod_grade::text)
  end as grade,  

  case when esc.dat_escala is not null then esc.e_in1-interval '2 hour' 
       when esp.cod_grade is not null then esp.g_in1_min
       else gra.g_in1_min 
  end as g_in1_min,  
  case when esc.dat_escala is not null then esc.e_in1 
       when esp.cod_grade is not null then esp.g_in1
       else gra.g_in1 
  end as g_in1,  
  case when esc.dat_escala is not null then esc.e_in1+interval '3 hour' 
       when esp.cod_grade is not null then esp.g_in1_max
       else gra.g_in1_max 
  end as g_in1_max,  
  (select b.hora 
     from "ponto"."batidas" b
    where b.data = fre.dat
      and b.num = fre.mat_func_reduzida::integer
    order by b.hora offset 0 limit 1
  ) as b_in1, 

  case when esc.dat_escala is not null then esc.e_out1-interval '2 hour' 
       when esp.cod_grade is not null then esp.g_out1_min
       else gra.g_out1_min 
  end as g_out1_min,  
  case when esc.dat_escala is not null then esc.e_out1 
       when esp.cod_grade is not null then esp.g_out1
       else gra.g_out1 
  end as g_out1,  
  case when esc.dat_escala is not null then esc.e_out1+interval '2 hour' 
       when esp.cod_grade is not null then esp.g_out1_max
       else gra.g_out1_max 
  end as g_out1_max,  
(select b.hora
   from "ponto"."batidas" b
  where b.data = fre.dat
    and b.num = fre.mat_func_reduzida::integer
  order by b.hora offset 1 limit 1
) AS b_out1, 

  case when esc.dat_escala is not null then esc.e_in2-interval '2 hour' 
       when esp.cod_grade is not null then esp.g_in2_min
       else gra.g_in2_min 
  end as g_in2_min,  
  case when esc.dat_escala is not null then esc.e_in2 
       when esp.cod_grade is not null then esp.g_in2
       else gra.g_in2 
  end as g_in2,  
  case when esc.dat_escala is not null then esc.e_in2+interval '2 hour' 
       when esp.cod_grade is not null then esp.g_in2_max
       else gra.g_in2_max 
  end as g_in2_max,  
(select b.hora
   from "ponto"."batidas" b
  where b.data = fre.dat
    and b.num = fre.mat_func_reduzida::integer
  order by b.hora offset 2 limit 1
) AS b_in2, 

  case when esc.dat_escala is not null then esc.e_out2-interval '2 hour' 
       when esp.cod_grade is not null then esp.g_out2_min
       else gra.g_out2_min 
  end as g_out2_min,  
  case when esc.dat_escala is not null then esc.e_out2 
       when esp.cod_grade is not null then esp.g_out2
       else gra.g_out2 
  end as g_out2,
  case when esc.dat_escala is not null then esc.e_out2+interval '2 hour' 
       when esp.cod_grade is not null then esp.g_out2_max
       else gra.g_out2_max 
  end as g_out2_max,
(select b.hora
   from "ponto"."batidas" b
  where b.data = fre.dat
    and b.num = fre.mat_func_reduzida::integer
  order by b.hora offset 3 limit 1
) as b_out2, 
 
  case when esc.dat_escala is not null then esc.log_12e
       when esp.cod_grade is not null then esp.log_12e
       else gra.log_12e
  end as log_12e,
  fre.log_feriado,
  
   (select count(DISTINCT to_char(b.hora, 'HH24:MI')) 
      from "ponto"."batidas" b 
     where b.data = fre.dat
       and b.num = fre.mat_func_reduzida::int) as qt_batidas

 FROM "ponto"."vw_funcionarios_dias" fre 
 LEFT JOIN "ponto"."vw_grades" gra on gra.cod_grade = fre.cod_grade and gra.num_dia = fre.num_dia 
 LEFT JOIN (SELECT hes.dat_especial,hes.mat_func, vgr.* 
              FROM "ponto"."horarios_especiais" hes, "ponto"."vw_grades" vgr
             WHERE vgr.cod_grade = hes.cod_grade 
               and to_char(hes.dat_especial,'D')::int = vgr.num_dia) esp 
        ON esp.dat_especial = fre.dat and esp.mat_func = fre.mat_func
 LEFT OUTER JOIN "ponto"."escalas" esc on esc.dat_escala = fre.dat and esc.mat_func = fre.mat_func ;

--
-- Definition for view vw_ocorrencias
--
CREATE OR REPLACE VIEW "ponto"."vw_ocorrencias" AS
SELECT ocorrencias.mat_func, 
       ocorrencias.dat_ocorr,
       COALESCE(ocorrencias.sai_ocorr, ocorrencias.ret_ocorr, '00:00:01'::time) AS sai_ocorr, 
       COALESCE(ocorrencias.ret_ocorr, ocorrencias.sai_ocorr, '23:59:59'::time) AS ret_ocorr,
       ocorrencias.motivo, 
       ocorrencias.dsc_ocorr, 
       ocorrencias.log_bco_horas
  FROM "ponto"."ocorrencias"
 WHERE id_status = 3;

--=========================================--
-- Definition for view vw_frequencias_base
--=========================================--
CREATE OR REPLACE VIEW "ponto"."vw_frequencias_base" AS
SELECT fre.dat_freq, fre.num_dia,
       fre.mat_func, fre.mat_func_reduzida, fre.nom_func, fre.grade,
       
       fre.g_in1,
       (SELECT min(bat.hora) 
          FROM "ponto"."batidas" bat
         WHERE bat.num  = fre.mat_func_reduzida::integer
           AND bat.data = fre.dat_freq
           AND bat.hora BETWEEN fre.g_in1_min AND fre.g_in1_max
       ) as in1, 
       (SELECT MAX(oco.motivo::text) AS max
          FROM "ponto"."vw_ocorrencias" oco
         WHERE oco.mat_func::text = fre.mat_func::text
           AND oco.dat_ocorr = fre.dat_freq 
           AND (fre.g_in1 is null or                
                (fre.g_in1 between oco.sai_ocorr and oco.ret_ocorr)
               )
       ) as log_in1, 

       fre.g_out1,
       case when fre.log_12e = 1 and fre.qt_batidas = 2 and grade <> 'ESCALA'
            then fre.b_out1
            else 
                 (SELECT max(bat.hora)
                    FROM "ponto"."batidas" bat
                   WHERE bat.num  = fre.mat_func_reduzida::integer
                     AND bat.data = fre.dat_freq 
                     AND bat.hora BETWEEN fre.g_out1_min AND fre.g_out1_max)
       end 
       as out1, 
       (SELECT max(oco.motivo::text) AS max
          FROM "ponto"."vw_ocorrencias" oco
         WHERE oco.mat_func::text = fre.mat_func::text
           AND oco.dat_ocorr = fre.dat_freq 
           AND ((fre.g_out1 is null) or
                (fre.g_out1 between oco.sai_ocorr and oco.ret_ocorr)
               )
               -- (oco.sai_ocorr <= fre.g_out1 or oco.ret_ocorr >= fre.g_out1) OR
               -- (oco.sai_ocorr <= fre.g_in1 and oco.ret_ocorr >= fre.g_out1))
        ) as log_out1, 
   
        fre.g_in2,
        (SELECT min(bat.hora) 
           FROM "ponto"."batidas" bat
          WHERE bat.num  = fre.mat_func_reduzida::integer
            AND bat.data = fre.dat_freq 
            AND bat.hora BETWEEN fre.g_in2_min AND fre.g_in2_max
        ) as in2, 
       (SELECT max(oco.motivo::text) AS max
          FROM "ponto"."vw_ocorrencias" oco
         WHERE oco.mat_func::text = fre.mat_func::text
           AND oco.dat_ocorr = fre.dat_freq
           AND ((fre.g_in2 is null and fre.log_12e = 2) or
                (fre.g_in2 between oco.sai_ocorr and oco.ret_ocorr)
               )
                --(oco.sai_ocorr <= fre.g_in2 or oco.ret_ocorr >= fre.g_in2) OR
                --(oco.sai_ocorr <= fre.g_in1 and oco.ret_ocorr >= fre.g_out2))
       ) as log_in2, 

       fre.g_out2,
       case when fre.log_12e = 2 and fre.qt_batidas >= 4 and grade <> 'ESCALA'
            then (SELECT max(bat.hora)
                    FROM "ponto"."batidas" bat
                   WHERE fre.mat_func_reduzida::integer = bat.num
                     AND bat.data = fre.dat_freq 
                     AND bat.hora > fre.g_in2)
            else (SELECT max(bat.hora)
                    FROM "ponto"."batidas" bat
                    WHERE fre.mat_func_reduzida::integer = bat.num
                      AND bat.data = fre.dat_freq 
                      AND bat.hora BETWEEN fre.g_out2_min AND fre.g_out2_max)
       end as out2, 
       (SELECT MAX(oco.motivo::text) AS max
          FROM "ponto"."vw_ocorrencias" oco
         WHERE oco.mat_func::text = fre.mat_func::text
           AND oco.dat_ocorr = fre.dat_freq
           AND ((fre.g_out2 is null and fre.log_12e = 2) or
                (fre.g_out2 between oco.sai_ocorr and oco.ret_ocorr)
               )
                --(oco.sai_ocorr <= fre.g_out2 or oco.ret_ocorr >= fre.g_out2))
       ) as log_out2,
   
       fre.log_12e, fre.log_feriado, fre.qt_batidas
FROM "ponto"."vw_funcionarios_escala" fre;
--FROM "ponto"."vw_funcionarios_escala_teste_batidas_qde" fre;

--
-- Definition for view vw_frequencias_ocor
--
CREATE OR REPLACE VIEW "ponto"."vw_frequencias_ocor" AS
SELECT vwf.dat_freq, vwf.num_dia, 
       vwf.mat_func, vwf.mat_func_reduzida, vwf.nom_func, vwf.grade,       

       vwf.g_in1, 
       CASE WHEN vwf.log_in1 IN ('F','A','V','E','L') 
            THEN NULL -- func. nao pode ter batida (abona e limpa)
         
            --JUSTIFICATIVA TOTAL (2 EXPEDIENTES)
            WHEN log_12e = 2 and log_in1 is not null and log_out1 is not null 
                          and log_in2 is not null and log_out2 is not null
            THEN vwf.in1

            --JUSTIFICATIVA TOTAL (1 EXPEDIENTE)
            WHEN log_12e = 1 AND log_in1 is not null and log_out1 is not null
            THEN COALESCE(vwf.in1,vwf.g_in1) 

            --JUSTIFICATIVA PARCIAL (1 EXPEDIENTE)
            WHEN log_12e = 1 
             AND COALESCE(log_in1,log_out1) IS NOT NULL
             AND (log_in1 is null OR log_out1 is null)
            THEN COALESCE(vwf.in1,vwf.g_in1) 
           
            --JUSTIFICATIVA PARCIAL (2 EXPEDIENTES)
            WHEN log_12e = 2
             AND COALESCE(log_in1,log_out1,log_in2, log_out2) IS NOT NULL
             AND (log_in1 is null OR log_out1 is null or log_in2 is null or log_out2 is null)
            THEN COALESCE(vwf.in1,vwf.g_in1) 
           
            ELSE vwf.in1 
       END AS in1, 
       vwf.log_in1, 
    
       vwf.g_out1,
       CASE WHEN vwf.log_out1 IN ('F','A','V','E','L') 
            THEN NULL
 
            WHEN (log_12e = 2 AND log_in1 is not null and log_out1 is not null AND log_in2 is not null and log_out2 is not null) OR
                 (log_12e = 2 AND log_in1 is null     and log_out1 is not null AND log_in2 is not null and log_out2 is     null) OR
                 (log_12e = 2 AND log_in1 is null     and log_out1 is not null AND log_in2 is not null and log_out2 is not null)
            THEN vwf.out1
              
            WHEN log_12e = 1 AND log_in1 is not null and log_out1 is not null
            THEN COALESCE(vwf.out1,vwf.g_out1) 
              
            WHEN log_12e = 1 
             AND COALESCE(log_in1,log_out1) IS NOT NULL
             AND (log_in1 is null OR log_out1 is null)
            THEN COALESCE(vwf.out1,vwf.g_out1) 
         
            WHEN log_12e = 2
             AND COALESCE(log_in1,log_out1,log_in2,log_out2) IS NOT NULL
             AND (log_in1 is null OR log_out1 is null or log_in2 is null or log_out2 is null)
            THEN COALESCE(vwf.out1,vwf.g_out1) 

            ELSE vwf.out1 
        END AS out1, 
        vwf.log_out1, 

        vwf.g_in2, 
        CASE WHEN vwf.log_in2 IN ('F','A','V','E','L') 
             THEN NULL
  
             WHEN (log_12e = 2 AND log_in1 is not null and log_out1 is not null AND log_in2 is not null and log_out2 is not null) OR
                  (log_12e = 2 AND log_in1 is null     and log_out1 is not null AND log_in2 is not null and log_out2 is     null) or
                  (log_12e = 2 AND log_in1 is null     and log_out1 is not null AND log_in2 is not null and log_out2 is not null)
             THEN vwf.in2
              
             WHEN log_12e = 1 AND log_in1 is not null and log_out1 is not null
             THEN COALESCE(vwf.in2,vwf.g_in2) 

             --JUSTIFICATIVA TARDE (2 EXPEDIENTE)
             WHEN log_12e = 2  AND  (LOG_12E=2 AND log_IN1 IS NULL AND log_OUT1 IS NULL AND log_IN2 IS NOT NULL AND log_OUT2 IS NOT NULL)
             THEN vwf.in2   
              
             --WHEN log_12e = 2
             -- AND COALESCE(log_in1,log_out1,log_in2, log_out2) IS NOT NULL
             -- AND (log_in1 is null OR log_out1 is null or log_in2 is null or log_out2 is null)
             --THEN COALESCE(vwf.in2,vwf.g_in2) 
             WHEN log_12e = 2 and log_in2 IS NOT NULL
             THEN COALESCE(vwf.in2,vwf.g_in2)

             ELSE vwf.in2 
        END AS in2, 
        vwf.log_in2,
 
        vwf.g_out2, 
        CASE WHEN vwf.log_out2 IN ('F','A','V','E','L') 
             THEN NULL

             WHEN log_12e = 2 AND log_in1 is not null and log_out1 is not null AND log_in2 is not null and log_out2 is not null
             THEN vwf.out2
              
             WHEN log_12e = 1 AND log_in1 is not null and log_out1 is not null
             THEN COALESCE(vwf.out2,vwf.g_out2)               

             --JUSTIFICATIVA TARDE (2 EXPEDIENTE)
             WHEN log_12e = 2  AND  (LOG_12E=2 AND log_IN1 IS NULL AND log_OUT1 IS NULL AND log_IN2 IS NOT NULL AND log_OUT2 IS NOT NULL)
             THEN vwf.out2   
              
             --WHEN log_12e = 2
             -- AND COALESCE(log_in1,log_out1,log_in2, log_out2) IS NOT NULL
             -- AND (log_in1 is null OR log_out1 is null or log_in2 is null or log_out2 is null)
             --THEN COALESCE(vwf.out2,vwf.g_out2) 
             --provavelmente usar este codigo para os demais : in1, out1 ...
             WHEN log_12e = 2 and log_out2 IS NOT NULL
             THEN COALESCE(vwf.out2,vwf.g_out2)
         
             ELSE vwf.out2 
        END AS out2,
        vwf.log_out2, 

        CASE WHEN vwf.log_feriado = 0 and vwf.log_12e > 0 and vwf.qt_batidas = 0 
              and log_in1 is null and log_out1 is null and log_in2 is null and log_out2 is null
             THEN 1 
             
             ELSE 0
        END as log_falta,
        vwf.log_12e,
        vwf.log_feriado,
        vwf.qt_batidas
FROM "ponto"."vw_frequencias_base" vwf;


--
-- Definition for view vw_frequencias
--
CREATE OR REPLACE VIEW "ponto"."vw_frequencias" AS
SELECT vwf.dat_freq, vwf.num_dia, 
       vwf.mat_func, vwf.mat_func_reduzida, vwf.nom_func, vwf.grade,       

    vwf.g_in1, 
    vwf.in1, 
    vwf.log_in1, 
    
    vwf.g_out1,
    vwf.out1,
    vwf.log_out1, 

    vwf.g_in2, 
    vwf.in2,
    vwf.log_in2,
 
    vwf.g_out2, 
    vwf.out2,
    vwf.log_out2, 
    
    --Calculo de atrasos
    case when vwf.log_falta = 1
         then interval '0 min'
         when (vwf.log_feriado = 0 and vwf.log_12e > 0)
         then (case when (vwf.g_in1 is not null AND vwf.log_in1 is NULL) and
                         ((vwf.in1 is null and vwf.log_in1 is NULL) OR
                          (vwf.out1 is null and vwf.log_out1 is NULL))
                    then interval '4 hour'
              
                    when (vwf.g_in1 is not null AND vwf.log_in1 is NULL) and
                         ((vwf.in1 - vwf.g_in1) > interval'10 minute')
                    then (vwf.in1 - vwf.g_in1)          
          
                    else interval '0 min'
               end)
              +    
              (case when (vwf.g_out1 is not null AND vwf.log_out1 is null) and
                         (vwf.in1 is not null and vwf.out1 is not null) and
                         ((vwf.g_out1 - vwf.out1) > interval'0 minute')
                    then (vwf.g_out1 - vwf.out1)
                    
                    else interval '0 min'
               end)
              +
              (case when (vwf.g_in2 is not null and vwf.log_in2 is NULL) AND
                         ((vwf.in2 is null and vwf.log_in2 is NULL) OR
                          (vwf.out2 is null and vwf.log_out2 is NULL))
                    then interval '4 hour'
                    
                    when (vwf.g_in2 is not null AND vwf.log_in2 is null) AND
                         ((vwf.in2 - vwf.g_in2) > interval'10 minute')
                    then (vwf.in2 - vwf.g_in2)
          
                   else interval '0 min'
               end)
              +
              (case when (vwf.g_out2 is not null AND vwf.log_out2 is null) and
                         (vwf.in2 is not null and vwf.out2 is not null) and
                         ((vwf.g_out2 - vwf.out2) > interval'0 minute')
                    then (vwf.g_out2 - vwf.out2)
          
                    else interval '0 min'
               end)
               
         when (vwf.log_feriado = 0 and vwf.log_12e = 0)
         then (case when (vwf.g_in1 is not null AND vwf.log_in1 is NULL) and
                         ((vwf.in1 - vwf.g_in1) > interval'10 minute')
                    then (vwf.in1 - vwf.g_in1)          
                    else interval '0 min'
               end)
              +    
              (case when (vwf.g_out1 is not null AND vwf.log_out1 is null) and
                         ((vwf.g_out1 - vwf.out1) > interval'0 minute')
                    then (vwf.g_out1 - vwf.out1)
                    else interval '0 min'
               end)
              +
              (case when (vwf.g_in2 is not null AND vwf.log_in2 is null) AND
                         ((vwf.in2 - vwf.g_in2) > interval'10 minute')
                    then (vwf.in2 - vwf.g_in2)          
                    else interval '0 min'
               end)
              +
              (case when (vwf.g_out2 is not null AND vwf.log_out2 is null) and
                         ((vwf.g_out2 - vwf.out2) > interval'0 minute')
                    then (vwf.g_out2 - vwf.out2)          
                    else interval '0 min'
               end)
               
         else interval '0 min'
    end as bco_desc,
    
    0 as log_13,
    vwf.log_falta,
    vwf.log_12e,
    vwf.log_feriado,
    vwf.qt_batidas,
    CASE WHEN (vwf.log_feriado = 0 and vwf.log_12e > 0) and
              ((vwf.g_in1 IS not null and in1 is null and log_in1 is null) or
              (vwf.g_out1 is not null and out1 is null and log_out1 is null) or
              (vwf.g_in2 is not null and in2 is null and log_in2 is null) or
              (vwf.g_out2 is not null and out2 is null and log_out2 is null))              
         THEN 1
         ELSE 0
    END AS ponto_incompleto,
    
    CASE WHEN vwf.log_feriado = 1
         THEN fer.descricao
         
         WHEN vwf.log_falta = 1
         THEN 'FALTA'
         
         WHEN (vwf.log_feriado = 0 and vwf.log_12e > 0) and
              ((vwf.g_in1 IS not null and in1 is null and log_in1 is null) or
              (vwf.g_out1 is not null and out1 is null and log_out1 is null) or
              (vwf.g_in2 is not null and in2 is null and log_in2 is null) or
              (vwf.g_out2 is not null and out2 is null and log_out2 is null))              
         THEN 'Incompleto'         
    
    WHEN (vwf.log_feriado = 0 and vwf.log_falta = 0) and
         (log_in1 is not null or log_in2 is not null  OR 
          log_out1 is not null or log_out2 is not null)
    THEN (select COALESCE(o.dsc_ocorr_rh,o.dsc_ocorr)
            from ponto.ocorrencias o 
           where o.mat_func =  vwf.mat_func 
             and o.dat_ocorr = vwf.dat_freq 
             and o.id_status=3
             and COALESCE(o.dsc_ocorr_rh,o.dsc_ocorr) is not null
           limit 1)
           
    ELSE null
    END AS OBSERVACAO
 FROM "ponto"."vw_frequencias_ocor" vwf
 LEFT JOIN "ponto"."feriados" fer ON fer.dat_feriado = vwf.dat_freq;
-- where vwf.dat_freq BETWEEN '01/03/2010' and '28/03/2010'
-- and vwf.mat_func like '%39291%'; 

--==================================================================--
--                       view alternativas                          --
--==================================================================--
-- View: ponto.vw_funcionarios_escala_teste

-- DROP VIEW ponto.vw_funcionarios_escala_teste;

CREATE OR REPLACE VIEW ponto.vw_funcionarios_escala_teste AS
 SELECT fre.dat AS dat_freq, fre.num_dia, fre.mat_func, fre.mat_func_reduzida, fre.nom_func,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN 'ESCALA'::text
            WHEN esp.cod_grade IS NOT NULL THEN 'E-'::text || esp.cod_grade::text
            ELSE 'G-'::text || fre.cod_grade::text
        END AS grade,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_in1 - '02:00:00'::interval
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_in1_min
            ELSE gra.g_in1_min
        END AS g_in1_min,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_in1
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_in1
            ELSE gra.g_in1
        END AS g_in1,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_in1 + '02:00:00'::interval
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_in1_max
            ELSE gra.g_in1_max
        END AS g_in1_max,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_out1 - '02:00:00'::interval
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_out1_min
            ELSE gra.g_out1_min
        END AS g_out1_min,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_out1
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_out1
            ELSE gra.g_out1
        END AS g_out1,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_out1 + '00:30:00'::interval
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_out1_max
            ELSE gra.g_out1_max
        END AS g_out1_max,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_in2 - '00:30:00'::interval
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_in2_min
            ELSE gra.g_in2_min
        END AS g_in2_min,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_in2
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_in2
            ELSE gra.g_in2
        END AS g_in2,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_in2 + '02:00:00'::interval
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_in2_max
            ELSE gra.g_in2_max
        END AS g_in2_max,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_out2 - '02:00:00'::interval
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_out2_min
            ELSE gra.g_out2_min
        END AS g_out2_min,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_out2
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_out2
            ELSE gra.g_out2
        END AS g_out2,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.e_out2 + '04:00:00'::interval
            WHEN esp.cod_grade IS NOT NULL THEN esp.g_out2_max
            ELSE gra.g_out2_max
        END AS g_out2_max,
        CASE
            WHEN esc.dat_escala IS NOT NULL THEN esc.log_12e::integer
            WHEN esp.cod_grade IS NOT NULL THEN esp.log_12e
            ELSE gra.log_12e
        END AS log_12e, fre.log_feriado
   FROM ponto.vw_funcionarios_dias fre
   LEFT JOIN ponto.vw_grades gra ON gra.cod_grade = fre.cod_grade AND gra.num_dia = fre.num_dia
   LEFT JOIN ( SELECT hes.dat_especial, hes.mat_func, vgr.cod_grade, vgr.num_dia, vgr.g_in1_min, vgr.g_in1, vgr.g_in1_max, vgr.g_out1_min, vgr.g_out1, vgr.g_out1_max, vgr.g_in2_min, vgr.g_in2, vgr.g_in2_max, vgr.g_out2_min, vgr.g_out2, vgr.g_out2_max, vgr.in1_13, vgr.out1_13, vgr.in2_13, vgr.out2_13, vgr.log_12e
      FROM ponto.horarios_especiais hes, ponto.vw_grades vgr
     WHERE vgr.cod_grade = hes.cod_grade AND to_char(hes.dat_especial::timestamp with time zone, 'D'::text)::integer = vgr.num_dia) esp ON esp.dat_especial = fre.dat AND esp.mat_func::text = fre.mat_func::text
   LEFT JOIN ponto.escalas esc ON esc.dat_escala = fre.dat AND esc.mat_func::text = fre.mat_func::text;

ALTER TABLE ponto.vw_funcionarios_escala_teste OWNER TO postgres;

-- View: ponto.vw_funcionarios_escala_teste_batidas

-- DROP VIEW ponto.vw_funcionarios_escala_teste_batidas;

CREATE OR REPLACE VIEW ponto.vw_funcionarios_escala_teste_batidas AS
 SELECT a.dat_freq, a.num_dia, a.mat_func, a.mat_func_reduzida, a.nom_func, a.grade, a.g_in1_min, a.g_in1, a.g_in1_max, ( SELECT min(b.hora) AS min
           FROM ponto.batidas b
          WHERE b.data = a.dat_freq AND b.num = a.mat_func_reduzida::integer AND b.hora >= a.g_in1_min AND b.hora <= a.g_in1_max) AS b_in1, a.g_out1_min, a.g_out1, a.g_out1_max, ( SELECT max(b.hora) AS max
           FROM ponto.batidas b
          WHERE b.data = a.dat_freq AND b.num = a.mat_func_reduzida::integer AND b.hora >= a.g_out1_min AND b.hora <= a.g_out1_max) AS b_out1, a.g_in2_min, a.g_in2, a.g_in2_max, ( SELECT min(b.hora) AS min
           FROM ponto.batidas b
          WHERE b.data = a.dat_freq AND b.num = a.mat_func_reduzida::integer AND b.hora >= a.g_in2_min AND b.hora <= a.g_in2_max) AS b_in2, a.g_out2_min, a.g_out2, a.g_out2_max, ( SELECT max(b.hora) AS max
           FROM ponto.batidas b
          WHERE b.data = a.dat_freq AND b.num = a.mat_func_reduzida::integer AND b.hora >= a.g_out2_min AND b.hora <= a.g_out2_max) AS b_out2, a.log_12e, a.log_feriado
   FROM ponto.vw_funcionarios_escala_teste a;

ALTER TABLE ponto.vw_funcionarios_escala_teste_batidas OWNER TO postgres;

-- View: ponto.vw_funcionarios_escala_teste_batidas_qde

-- DROP VIEW ponto.vw_funcionarios_escala_teste_batidas_qde;

CREATE OR REPLACE VIEW ponto.vw_funcionarios_escala_teste_batidas_qde AS
 SELECT vw_funcionarios_escala_teste_batidas.dat_freq, vw_funcionarios_escala_teste_batidas.num_dia, vw_funcionarios_escala_teste_batidas.mat_func, vw_funcionarios_escala_teste_batidas.mat_func_reduzida, vw_funcionarios_escala_teste_batidas.nom_func, vw_funcionarios_escala_teste_batidas.grade, vw_funcionarios_escala_teste_batidas.g_in1_min, vw_funcionarios_escala_teste_batidas.g_in1, vw_funcionarios_escala_teste_batidas.g_in1_max, vw_funcionarios_escala_teste_batidas.b_in1, vw_funcionarios_escala_teste_batidas.g_out1_min, vw_funcionarios_escala_teste_batidas.g_out1, vw_funcionarios_escala_teste_batidas.g_out1_max, vw_funcionarios_escala_teste_batidas.b_out1, vw_funcionarios_escala_teste_batidas.g_in2_min, vw_funcionarios_escala_teste_batidas.g_in2, vw_funcionarios_escala_teste_batidas.g_in2_max, vw_funcionarios_escala_teste_batidas.b_in2, vw_funcionarios_escala_teste_batidas.g_out2_min, vw_funcionarios_escala_teste_batidas.g_out2, vw_funcionarios_escala_teste_batidas.g_out2_max, vw_funcionarios_escala_teste_batidas.b_out2, vw_funcionarios_escala_teste_batidas.log_12e, vw_funcionarios_escala_teste_batidas.log_feriado, "isnull"(vw_funcionarios_escala_teste_batidas.b_in1::text, '0'::text, '1'::text)::integer + "isnull"(vw_funcionarios_escala_teste_batidas.b_in2::text, '0'::text, '1'::text)::integer + "isnull"(vw_funcionarios_escala_teste_batidas.b_out1::text, '0'::text, '1'::text)::integer + "isnull"(vw_funcionarios_escala_teste_batidas.b_out2::text, '0'::text, '1'::text)::integer AS qt_batidas
   FROM ponto.vw_funcionarios_escala_teste_batidas;

ALTER TABLE ponto.vw_funcionarios_escala_teste_batidas_qde OWNER TO postgres;

--==============================================================--
--                                INDEX                         -- 
--==============================================================--
CREATE INDEX "ocorrencias_idx" ON "ponto"."ocorrencias"  USING btree ("mat_func", "dat_ocorr");
CREATE INDEX ocorrencias_idx_num ON batidas USING btree (num,data);

--==============================================================--
--                                PK E UK                       -- 
--==============================================================--
ALTER TABLE ONLY batidas ADD CONSTRAINT batidas_pk PRIMARY KEY (data, hora, num);
ALTER TABLE ONLY escalas ADD CONSTRAINT escalas_pk PRIMARY KEY (dat_escala, mat_func);
ALTER TABLE ONLY feriados ADD CONSTRAINT feriados_pk PRIMARY KEY (dat_feriado);
ALTER TABLE ONLY frequencias ADD CONSTRAINT frequencias_pk PRIMARY KEY (dat_freq, mat_func);

ALTER TABLE ONLY funcionarios ADD CONSTRAINT funcionarios_pk PRIMARY KEY (mat_func);
ALTER TABLE ONLY funcionarios ADD CONSTRAINT funcionarios_uk_mat_func_reduzida UNIQUE (mat_func_reduzida);
ALTER TABLE ONLY funcionarios ADD CONSTRAINT funcionarios_uk_num_func UNIQUE (num_func);

ALTER TABLE ONLY grade_horarios ADD CONSTRAINT grade_horarios_pk PRIMARY KEY (cod_grade, num_dia);
ALTER TABLE ONLY grades ADD CONSTRAINT grades_pk PRIMARY KEY (cod_grade);
ALTER TABLE ONLY historicos  ADD CONSTRAINT historicos_pk PRIMARY KEY (dat_mes, mat_func);
ALTER TABLE ONLY horarios_especiais ADD CONSTRAINT horarios_especiais_pk PRIMARY KEY (dat_especial, mat_func);
ALTER TABLE ONLY lotacoes ADD CONSTRAINT lotacoes_pk PRIMARY KEY (cod_lotacao);
ALTER TABLE ONLY mes_processado ADD CONSTRAINT mes_processado_pk PRIMARY KEY (dat_mes);
ALTER TABLE ONLY ocorrencias ADD CONSTRAINT ocorrencias_pk PRIMARY KEY (id);
ALTER TABLE ONLY ocorrencias ADD CONSTRAINT ocorrencias_uk UNIQUE (mat_func, dat_ocorr, sai_ocorr, ret_ocorr, motivo);
ALTER TABLE ONLY orgaos ADD CONSTRAINT orgaos_pk PRIMARY KEY (cd_org);
ALTER TABLE ONLY status ADD CONSTRAINT status_pkey PRIMARY KEY (id);

--==============================================================--
--                                FK                            -- 
--==============================================================--
ALTER TABLE ONLY escalas ADD CONSTRAINT escalas_fk_funcionarios FOREIGN KEY (mat_func) REFERENCES funcionarios(mat_func);
ALTER TABLE ONLY frequencias ADD CONSTRAINT frequencias_fk_funcionarios FOREIGN KEY (mat_func) REFERENCES funcionarios(mat_func);
ALTER TABLE ONLY funcionarios ADD CONSTRAINT funcionarios_fk_grades FOREIGN KEY (cod_grade) REFERENCES grades(cod_grade);
ALTER TABLE ONLY funcionarios ADD CONSTRAINT funcionarios_fk_lotacao FOREIGN KEY (lotacao) REFERENCES lotacoes(cod_lotacao);
ALTER TABLE ONLY grade_horarios ADD CONSTRAINT grade_horarios_fk_grades FOREIGN KEY (cod_grade) REFERENCES grades(cod_grade);
ALTER TABLE ONLY historicos  ADD CONSTRAINT historicos_fk_funcionarios FOREIGN KEY (mat_func) REFERENCES funcionarios(mat_func);
ALTER TABLE ONLY historicos ADD CONSTRAINT historicos_fk_grades FOREIGN KEY (cod_grade) REFERENCES grades(cod_grade);
ALTER TABLE ONLY horarios_especiais ADD CONSTRAINT horarios_especiais_fk_funcionarios FOREIGN KEY (mat_func) REFERENCES funcionarios(mat_func);
ALTER TABLE ONLY horarios_especiais ADD CONSTRAINT horarios_especiais_fk_grades FOREIGN KEY (cod_grade) REFERENCES grades(cod_grade);
ALTER TABLE ONLY ocorrencias ADD CONSTRAINT ocorrencias_fk_funcionarios FOREIGN KEY (mat_func) REFERENCES funcionarios(mat_func);
ALTER TABLE ONLY ocorrencias ADD CONSTRAINT ocorrencias_fk_status FOREIGN KEY (id_status) REFERENCES status(id);
ALTER TABLE ONLY lotacoes ADD CONSTRAINT lotacoes_fk_funcionarios FOREIGN KEY (orientador) REFERENCES funcionarios(mat_func);

--==============================================================--
--                                CK                            --
--==============================================================--
ALTER TABLE ONLY ocorrencias ADD CONSTRAINT "ocorrencias_ck_motivo" CHECK (motivo IN ('A','F','E','L','J','P','S','T','R','V','O','X'));
ALTER TABLE ONLY batidas ADD CONSTRAINT "batidas_ck_data" CHECK (data <= CURRENT_DATE);
ALTER TABLE ONLY escalas ADD CONSTRAINT escalas_log_12e_ck CHECK (log_12e >= 0 AND log_12e <= 2);
ALTER TABLE ONLY funcionarios ADD CONSTRAINT "funcionarios_log_registro_ck" CHECK (log_registro =1 or log_registro = 0);

--==============================================================--
--                            TRIGGERS                          -- 
--==============================================================--

CREATE TRIGGER tg_mes_processado_AI 
       AFTER INSERT ON "ponto"."mes_processado" 
       FOR EACH ROW
       EXECUTE PROCEDURE "ponto"."fun_processa_mes"();

CREATE TRIGGER tg_mes_processado_AD 
       AFTER DELETE ON "ponto"."mes_processado" 
       FOR EACH ROW
       EXECUTE PROCEDURE "ponto"."fun_limpa_mes"();

--
-- Comments
--
COMMENT ON COLUMN "ponto"."frequencias"."mat_func" IS 'Número da matrícula';
COMMENT ON COLUMN "ponto"."frequencias"."dat_freq" IS 'Data da freqüência';
COMMENT ON COLUMN "ponto"."frequencias"."in1" IS 'Hora de Entrada do 1o. turno';
COMMENT ON COLUMN "ponto"."frequencias"."log_in1" IS 'nulo, A, J, P, S,T,X,R,L,E,F';
COMMENT ON COLUMN "ponto"."frequencias"."out1" IS 'Hora de Saída do 1o. turno';
COMMENT ON COLUMN "ponto"."frequencias"."log_out1" IS 'nulo, A, J, P, S,T,X,R,L,E,F';
COMMENT ON COLUMN "ponto"."frequencias"."in2" IS 'Hora de Entrada do 1o. turno';
COMMENT ON COLUMN "ponto"."frequencias"."log_in2" IS 'nulo, A, J, P, S,T,X,R,L,E,F';
COMMENT ON COLUMN "ponto"."frequencias"."out2" IS 'Hora de Saída do 2o. turno';
COMMENT ON COLUMN "ponto"."frequencias"."log_out2" IS 'nulo, A, J, P, S,T,X,R,L,E,F';
COMMENT ON COLUMN "ponto"."frequencias"."bco_desc" IS 'nulo, A, J, P, S,T,X,R,L,E,F';
COMMENT ON COLUMN "ponto"."frequencias"."log_13" IS 'Quantidade 1/3 efetuados';
COMMENT ON COLUMN "ponto"."frequencias"."num_dia" IS 'Quantidade 1/3 efetuados';
COMMENT ON COLUMN "ponto"."frequencias"."log_feriado" IS 'Se é ou não Feriado';
COMMENT ON COLUMN "ponto"."frequencias"."log_falta" IS 'Se é ou não Falta';

COMMENT ON COLUMN "ponto"."grade_horarios"."log_12e" IS '1 expediente ou 2 expedientes';

COMMENT ON COLUMN "ponto"."funcionarios"."mat_func" IS 'Matrícula do Funcionário';

COMMENT ON COLUMN "ponto"."historicos"."qtd_fe" IS 'Quantidade de Férias';
COMMENT ON COLUMN "ponto"."historicos"."qtd_fo" IS 'Quantidade de Folga';

COMMENT ON COLUMN "ponto"."horarios_especiais"."mat_func" IS 'Matrícula do Funcionário';
COMMENT ON COLUMN "ponto"."horarios_especiais"."dat_especial" IS 'Data do Horário Especial';
COMMENT ON COLUMN "ponto"."horarios_especiais"."cod_grade" IS 'Código da Grade de Horário trabalhada no Dia pelo Funcionário';

COMMENT ON COLUMN "ponto"."ocorrencias"."motivo" IS '<A>testado,<F>erias,<E>special,<L>içenca,<J>ustificativa,<P>articular,<S>ervico,<T>reinamento,<R>ota,<V>iagem,O-folga,X-cortar ponto';

--
-- INSERTS
--
INSERT INTO "ponto"."status" ("tipo", "id", "descricao") VALUES ('AGUARDANDO_AUTORIZACAO', 1, 'Aguardando Autorização');
INSERT INTO "ponto"."status" ("tipo", "id", "descricao") VALUES ('AGUARDANDO_VALIDACAO', 2, 'Aguardando Validação');
INSERT INTO "ponto"."status" ("tipo", "id", "descricao") VALUES ('VALIDADO', 3, 'Validado');
INSERT INTO "ponto"."status" ("tipo", "id", "descricao") VALUES ('CANCELADO', 4, 'Cancelado');

INSERT INTO "ponto"."lotacoes" ("cod_lotacao", "descricao", "sigla", "orientador") VALUES ('RH', 'SERH', 'RH', NULL);
INSERT INTO "ponto"."grades" ("cod_grade", "descricao") VALUES (1, 'HORARIO PADRAO');
INSERT INTO "ponto"."grade_horarios" ("cod_grade", "num_dia", "g_in1_min", "g_in1", "g_in1_max", "g_out1_min", "g_out1", "g_out1_max", "g_in2_min", "g_in2", "g_in2_max", "g_out2_min", "g_out2", "g_out2_max", "in1_13", "out1_13", "in2_13", "out2_13", "log_12e")
VALUES (1, 9, '00:00:01', '08:00:00', '10:59:59', '11:00:00', '12:00:00', '12:29:59', '12:30:00', '13:00:00', '15:00:00', '15:00:00', '17:00:00', '23:59:59', '09:20:00', '10:40:00', '14:20:00', '15:40:00', 2);

--==============================================================--
--                                FIM                           -- 
--==============================================================--
