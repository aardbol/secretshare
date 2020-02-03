-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.9.2
-- PostgreSQL version: 11.0
-- Project Site: pgmodeler.io
-- Model Author: ---


-- Database creation must be done outside a multicommand file.
-- These commands were put in this file only as a convenience.
-- -- object: secretshare | type: DATABASE --
-- DROP DATABASE IF EXISTS secretshare;
-- CREATE DATABASE secretshare;
-- -- ddl-end --
-- 

-- object: public.secrets | type: TABLE --
-- DROP TABLE IF EXISTS public.secrets CASCADE;
CREATE TABLE public.secrets (
	id varchar(6) NOT NULL,
	data text NOT NULL,
	created timestamp NOT NULL DEFAULT now(),
	expires timestamp NOT NULL,
	CONSTRAINT pk_secret_id PRIMARY KEY (id)

);
-- ddl-end --
COMMENT ON COLUMN public.secrets.id IS E'6 character length uuid';
-- ddl-end --
COMMENT ON COLUMN public.secrets.data IS E'AES encrypted version of the secret';
-- ddl-end --
-- ALTER TABLE public.secrets OWNER TO postgres;
-- ddl-end --

-- object: public.access | type: TABLE --
-- DROP TABLE IF EXISTS public.access CASCADE;
CREATE TABLE public.access (
	id serial NOT NULL,
	ip varchar(45) NOT NULL,
	"time" timestamp NOT NULL DEFAULT now(),
	secret varchar(6) NOT NULL,
	CONSTRAINT pk_access_id PRIMARY KEY (id)

);
-- ddl-end --
-- ALTER TABLE public.access OWNER TO postgres;
-- ddl-end --

-- object: fk_access_secret | type: CONSTRAINT --
-- ALTER TABLE public.access DROP CONSTRAINT IF EXISTS fk_access_secret CASCADE;
ALTER TABLE public.access ADD CONSTRAINT fk_access_secret FOREIGN KEY (secret)
REFERENCES public.secrets (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --


