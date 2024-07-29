CREATE DATABASE usersDbFirst;

DROP TABLE IF EXISTS public.users_first;

CREATE TABLE IF NOT EXISTS public.users_first
(
    user_id character varying COLLATE pg_catalog."default",
    login character varying COLLATE pg_catalog."default",
    first_name character varying COLLATE pg_catalog."default",
    last_name character varying COLLATE pg_catalog."default"
);

INSERT INTO public.users_first VALUES('111', 'login', 'first name', 'last name')
