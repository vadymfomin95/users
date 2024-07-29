CREATE DATABASE usersDbSecond;

DROP TABLE IF EXISTS public.users_second;

CREATE TABLE IF NOT EXISTS public.users_second
(
    ldap_id character varying COLLATE pg_catalog."default",
    ldap_login character varying COLLATE pg_catalog."default",
    name character varying COLLATE pg_catalog."default",
    surname character varying COLLATE pg_catalog."default"
);

INSERT INTO public.users_second VALUES('222', 'ldap login', 'ldap first name', 'ldap last name')
