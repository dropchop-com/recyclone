--
-- PostgreSQL database dump
--

-- Dumped from database version 17.4 (Debian 17.4-1.pgdg120+2)
-- Dumped by pg_dump version 17.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: country; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.country (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    code character varying(255) NOT NULL,
    lang character varying(255),
    title character varying(255)
);


ALTER TABLE public.country OWNER TO recyclone;

--
-- Name: country_l; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.country_l (
    created timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    title character varying(1024),
    fk_country_code character varying(255) NOT NULL,
    lang character varying(255)
);


ALTER TABLE public.country_l OWNER TO recyclone;

--
-- Name: country_t; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.country_t (
    idx integer NOT NULL,
    fk_tag_uuid uuid NOT NULL,
    fk_country_code character varying(255) NOT NULL
);


ALTER TABLE public.country_t OWNER TO recyclone;

--
-- Name: dummy; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.dummy (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    code character varying(255) NOT NULL,
    description character varying(255),
    lang character varying(255),
    title character varying(255)
);


ALTER TABLE public.dummy OWNER TO recyclone;

--
-- Name: dummy_l; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.dummy_l (
    created timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    title character varying(1024),
    description character varying(8096),
    fk_dummy_code character varying(255) NOT NULL,
    lang character varying(255)
);


ALTER TABLE public.dummy_l OWNER TO recyclone;

--
-- Name: language; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.language (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    code character varying(255) NOT NULL,
    lang character varying(255),
    title character varying(255)
);


ALTER TABLE public.language OWNER TO recyclone;

--
-- Name: language_l; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.language_l (
    created timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    title character varying(1024),
    fk_language_code character varying(255) NOT NULL,
    lang character varying(255)
);


ALTER TABLE public.language_l OWNER TO recyclone;

--
-- Name: language_t; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.language_t (
    idx integer NOT NULL,
    fk_tag_uuid uuid NOT NULL,
    fk_language_code character varying(255) NOT NULL
);


ALTER TABLE public.language_t OWNER TO recyclone;

--
-- Name: security_action; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_action (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    code character varying(255) NOT NULL,
    description character varying(255),
    lang character varying(255),
    title character varying(255)
);


ALTER TABLE public.security_action OWNER TO recyclone;

--
-- Name: security_action_l; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_action_l (
    created timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    title character varying(1024),
    description character varying(8096),
    fk_security_action_code character varying(255) NOT NULL,
    lang character varying(255)
);


ALTER TABLE public.security_action_l OWNER TO recyclone;

--
-- Name: security_domain; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_domain (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    code character varying(255) NOT NULL,
    description character varying(255),
    lang character varying(255),
    title character varying(255)
);


ALTER TABLE public.security_domain OWNER TO recyclone;

--
-- Name: security_domain_l; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_domain_l (
    created timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    title character varying(1024),
    description character varying(8096),
    fk_security_domain_code character varying(255) NOT NULL,
    lang character varying(255)
);


ALTER TABLE public.security_domain_l OWNER TO recyclone;

--
-- Name: security_domain_security_action; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_domain_security_action (
    fk_security_action_code character varying(255) NOT NULL,
    fk_security_domain_code character varying(255) NOT NULL
);


ALTER TABLE public.security_domain_security_action OWNER TO recyclone;

--
-- Name: security_permission; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_permission (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    uuid uuid NOT NULL,
    description character varying(255),
    fk_security_action_code character varying(255),
    fk_security_domain_code character varying(255),
    lang character varying(255),
    title character varying(255)
);


ALTER TABLE public.security_permission OWNER TO recyclone;

--
-- Name: security_permission_instances; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_permission_instances (
    fk_security_permission_uuid uuid NOT NULL,
    instance character varying(255)
);


ALTER TABLE public.security_permission_instances OWNER TO recyclone;

--
-- Name: security_permission_l; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_permission_l (
    created timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    fk_security_permission_uuid uuid NOT NULL,
    title character varying(1024),
    description character varying(8096),
    lang character varying(255)
);


ALTER TABLE public.security_permission_l OWNER TO recyclone;

--
-- Name: security_role; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_role (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    code character varying(255) NOT NULL,
    description character varying(255),
    lang character varying(255),
    title character varying(255)
);


ALTER TABLE public.security_role OWNER TO recyclone;

--
-- Name: security_role_l; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_role_l (
    created timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    title character varying(1024),
    description character varying(8096),
    fk_security_role_code character varying(255) NOT NULL,
    lang character varying(255)
);


ALTER TABLE public.security_role_l OWNER TO recyclone;

--
-- Name: security_role_node; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_role_node (
    max_parent_instance_level integer,
    fk_role_node_uuid uuid,
    uuid uuid NOT NULL,
    entity character varying(255),
    entity_id character varying(255),
    target character varying(255) NOT NULL,
    target_id character varying(255)
);


ALTER TABLE public.security_role_node OWNER TO recyclone;

--
-- Name: security_role_node_permission; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_role_node_permission (
    allowed boolean NOT NULL,
    fk_permission_uuid uuid NOT NULL,
    fk_role_node_uuid uuid NOT NULL,
    uuid uuid NOT NULL,
    type character varying(31) NOT NULL,
    target character varying(255),
    target_id character varying(255)
);


ALTER TABLE public.security_role_node_permission OWNER TO recyclone;

--
-- Name: security_role_security_permission; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_role_security_permission (
    fk_security_permission_uuid uuid NOT NULL,
    fk_security_role_code character varying(255) NOT NULL
);


ALTER TABLE public.security_role_security_permission OWNER TO recyclone;

--
-- Name: security_user; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_user (
    is_female boolean,
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    uuid uuid NOT NULL,
    default_email character varying(255),
    default_phone character varying(255),
    first_name character varying(255),
    fk_country_code character varying(255),
    fk_language_code character varying(255),
    initials character varying(255),
    last_name character varying(255),
    maiden_name character varying(255),
    middle_name character varying(255),
    title character varying(255)
);


ALTER TABLE public.security_user OWNER TO recyclone;

--
-- Name: security_user_a; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_user_a (
    fk_security_user_uuid uuid NOT NULL,
    name character varying(255),
    type character varying(255),
    value character varying(255),
    CONSTRAINT security_user_a_type_check CHECK (((type)::text = ANY ((ARRAY['str'::character varying, 'bool'::character varying, 'num'::character varying, 'date'::character varying, 'set'::character varying, 'list'::character varying])::text[])))
);


ALTER TABLE public.security_user_a OWNER TO recyclone;

--
-- Name: security_user_account; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_user_account (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    fk_security_user_uuid uuid NOT NULL,
    uuid uuid NOT NULL,
    type character varying(31) NOT NULL,
    login_name character varying(255),
    password character varying(255),
    title character varying(255),
    token character varying(255)
);


ALTER TABLE public.security_user_account OWNER TO recyclone;

--
-- Name: security_user_role; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_user_role (
    fk_user_uuid uuid NOT NULL,
    fk_role_code character varying(255) NOT NULL
);


ALTER TABLE public.security_user_role OWNER TO recyclone;

--
-- Name: security_user_t; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.security_user_t (
    idx integer NOT NULL,
    fk_security_user_uuid uuid NOT NULL,
    fk_tag_uuid uuid NOT NULL
);


ALTER TABLE public.security_user_t OWNER TO recyclone;

--
-- Name: tag; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.tag (
    created timestamp(6) with time zone,
    deactivated timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    uuid uuid NOT NULL,
    type character varying(31) NOT NULL,
    description character varying(255),
    lang character varying(255),
    name character varying(255),
    title character varying(255)
);


ALTER TABLE public.tag OWNER TO recyclone;

--
-- Name: tag_a; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.tag_a (
    fk_tag_uuid uuid NOT NULL,
    name character varying(255),
    type character varying(255),
    value character varying(255),
    CONSTRAINT tag_a_type_check CHECK (((type)::text = ANY ((ARRAY['str'::character varying, 'bool'::character varying, 'num'::character varying, 'date'::character varying, 'set'::character varying, 'list'::character varying])::text[])))
);


ALTER TABLE public.tag_a OWNER TO recyclone;

--
-- Name: tag_l; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.tag_l (
    created timestamp(6) with time zone,
    modified timestamp(6) with time zone,
    fk_tag_uuid uuid NOT NULL,
    title character varying(1024),
    description character varying(8096),
    lang character varying(255)
);


ALTER TABLE public.tag_l OWNER TO recyclone;

--
-- Name: tag_t; Type: TABLE; Schema: public; Owner: recyclone
--

CREATE TABLE public.tag_t (
    idx integer NOT NULL,
    fk_next_tag_uuid uuid NOT NULL,
    fk_tag_uuid uuid NOT NULL
);


ALTER TABLE public.tag_t OWNER TO recyclone;

--
-- Name: country country_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_pkey PRIMARY KEY (code);


--
-- Name: country_t country_t_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country_t
    ADD CONSTRAINT country_t_pkey PRIMARY KEY (idx, fk_country_code);


--
-- Name: dummy dummy_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.dummy
    ADD CONSTRAINT dummy_pkey PRIMARY KEY (code);


--
-- Name: language language_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language
    ADD CONSTRAINT language_pkey PRIMARY KEY (code);


--
-- Name: language_t language_t_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language_t
    ADD CONSTRAINT language_t_pkey PRIMARY KEY (idx, fk_language_code);


--
-- Name: security_action security_action_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_action
    ADD CONSTRAINT security_action_pkey PRIMARY KEY (code);


--
-- Name: security_domain security_domain_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_domain
    ADD CONSTRAINT security_domain_pkey PRIMARY KEY (code);


--
-- Name: security_domain_security_action security_domain_security_action_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_domain_security_action
    ADD CONSTRAINT security_domain_security_action_pkey PRIMARY KEY (fk_security_action_code, fk_security_domain_code);


--
-- Name: security_permission security_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission
    ADD CONSTRAINT security_permission_pkey PRIMARY KEY (uuid);


--
-- Name: security_role_node_permission security_role_node_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_node_permission
    ADD CONSTRAINT security_role_node_permission_pkey PRIMARY KEY (uuid);


--
-- Name: security_role_node security_role_node_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_node
    ADD CONSTRAINT security_role_node_pkey PRIMARY KEY (uuid);


--
-- Name: security_role security_role_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role
    ADD CONSTRAINT security_role_pkey PRIMARY KEY (code);


--
-- Name: security_role_security_permission security_role_security_permission_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_security_permission
    ADD CONSTRAINT security_role_security_permission_pkey PRIMARY KEY (fk_security_permission_uuid, fk_security_role_code);


--
-- Name: security_user_account security_user_account_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_account
    ADD CONSTRAINT security_user_account_pkey PRIMARY KEY (uuid);


--
-- Name: security_user security_user_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user
    ADD CONSTRAINT security_user_pkey PRIMARY KEY (uuid);


--
-- Name: security_user_role security_user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_role
    ADD CONSTRAINT security_user_role_pkey PRIMARY KEY (fk_user_uuid, fk_role_code);


--
-- Name: security_user_t security_user_t_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_t
    ADD CONSTRAINT security_user_t_pkey PRIMARY KEY (idx, fk_security_user_uuid);


--
-- Name: tag tag_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_pkey PRIMARY KEY (uuid);


--
-- Name: tag_t tag_t_pkey; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_t
    ADD CONSTRAINT tag_t_pkey PRIMARY KEY (idx, fk_tag_uuid);


--
-- Name: country_l uq_country_l_fk_language_code_lang; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country_l
    ADD CONSTRAINT uq_country_l_fk_language_code_lang UNIQUE (fk_country_code, lang);


--
-- Name: country_t uq_country_t_fk_country_code_fk_tag_uuid; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country_t
    ADD CONSTRAINT uq_country_t_fk_country_code_fk_tag_uuid UNIQUE (fk_country_code, fk_tag_uuid);


--
-- Name: dummy_l uq_dummy_l_fk_language_code_lang; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.dummy_l
    ADD CONSTRAINT uq_dummy_l_fk_language_code_lang UNIQUE (fk_dummy_code, lang);


--
-- Name: language_l uq_language_l_fk_language_code_lang; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language_l
    ADD CONSTRAINT uq_language_l_fk_language_code_lang UNIQUE (fk_language_code, lang);


--
-- Name: language_t uq_language_t_fk_language_code_fk_tag_uuid; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language_t
    ADD CONSTRAINT uq_language_t_fk_language_code_fk_tag_uuid UNIQUE (fk_language_code, fk_tag_uuid);


--
-- Name: security_action_l uq_security_action_l_fk_language_code_lang; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_action_l
    ADD CONSTRAINT uq_security_action_l_fk_language_code_lang UNIQUE (fk_security_action_code, lang);


--
-- Name: security_domain_l uq_security_domain_l_fk_language_code_lang; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_domain_l
    ADD CONSTRAINT uq_security_domain_l_fk_language_code_lang UNIQUE (fk_security_domain_code, lang);


--
-- Name: security_permission_instances uq_security_permission_instances_instance; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission_instances
    ADD CONSTRAINT uq_security_permission_instances_instance UNIQUE (fk_security_permission_uuid, instance);


--
-- Name: security_permission_l uq_security_permission_l_fk_language_code_lang; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission_l
    ADD CONSTRAINT uq_security_permission_l_fk_language_code_lang UNIQUE (fk_security_permission_uuid, lang);


--
-- Name: security_role_l uq_security_role_l_fk_language_code_lang; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_l
    ADD CONSTRAINT uq_security_role_l_fk_language_code_lang UNIQUE (fk_security_role_code, lang);


--
-- Name: security_user_a uq_security_user_a_fk_user_uuid_name; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_a
    ADD CONSTRAINT uq_security_user_a_fk_user_uuid_name UNIQUE (fk_security_user_uuid, name);


--
-- Name: security_user_t uq_security_user_t_fk_security_user_uuid_fk_tag_uuid; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_t
    ADD CONSTRAINT uq_security_user_t_fk_security_user_uuid_fk_tag_uuid UNIQUE (fk_security_user_uuid, fk_tag_uuid);


--
-- Name: tag_a uq_tag_a_fk_tag_uuid_name; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_a
    ADD CONSTRAINT uq_tag_a_fk_tag_uuid_name UNIQUE (fk_tag_uuid, name);


--
-- Name: tag_l uq_tag_l_fk_language_code_lang; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_l
    ADD CONSTRAINT uq_tag_l_fk_language_code_lang UNIQUE (fk_tag_uuid, lang);


--
-- Name: tag_t uq_tag_t_fk_tag_uuid_fk_next_tag_uuid; Type: CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_t
    ADD CONSTRAINT uq_tag_t_fk_tag_uuid_fk_next_tag_uuid UNIQUE (fk_tag_uuid, fk_next_tag_uuid);


--
-- Name: security_user country_code_fk; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user
    ADD CONSTRAINT country_code_fk FOREIGN KEY (fk_country_code) REFERENCES public.country(code);


--
-- Name: country country_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country
    ADD CONSTRAINT country_fk_language_code FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: country_l country_l_fk_country_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country_l
    ADD CONSTRAINT country_l_fk_country_code FOREIGN KEY (fk_country_code) REFERENCES public.country(code);


--
-- Name: country_t country_t_fk_country_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country_t
    ADD CONSTRAINT country_t_fk_country_code FOREIGN KEY (fk_country_code) REFERENCES public.country(code);


--
-- Name: country_t country_t_fk_tag_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country_t
    ADD CONSTRAINT country_t_fk_tag_uuid FOREIGN KEY (fk_tag_uuid) REFERENCES public.tag(uuid);


--
-- Name: dummy dummy_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.dummy
    ADD CONSTRAINT dummy_fk_language_code FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: dummy_l dummy_l_fk_country_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.dummy_l
    ADD CONSTRAINT dummy_l_fk_country_code FOREIGN KEY (fk_dummy_code) REFERENCES public.dummy(code);


--
-- Name: security_role_l fkd3ywj2rkxndn9g5ypnxxoqcs2; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_l
    ADD CONSTRAINT fkd3ywj2rkxndn9g5ypnxxoqcs2 FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: language_l fkema1ix2g3yrhrh54tcggi4bik; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language_l
    ADD CONSTRAINT fkema1ix2g3yrhrh54tcggi4bik FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: security_domain_l fkgh7pnilvx6a2h948f754gdq15; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_domain_l
    ADD CONSTRAINT fkgh7pnilvx6a2h948f754gdq15 FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: security_permission_l fkhhdi0mmrvwithyj2bdwk8v4jr; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission_l
    ADD CONSTRAINT fkhhdi0mmrvwithyj2bdwk8v4jr FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: security_action_l fkkp1ksq96x0f5k1anx7bcl0l9u; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_action_l
    ADD CONSTRAINT fkkp1ksq96x0f5k1anx7bcl0l9u FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: country_l fko9wqoy8tdhxhun2ntjqem5smw; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.country_l
    ADD CONSTRAINT fko9wqoy8tdhxhun2ntjqem5smw FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: tag_l fkq9yfnl1nxij95f3883qv8p6oq; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_l
    ADD CONSTRAINT fkq9yfnl1nxij95f3883qv8p6oq FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: dummy_l fkteiixdghmklptbd7k0ngedkfu; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.dummy_l
    ADD CONSTRAINT fkteiixdghmklptbd7k0ngedkfu FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: security_user language_code_fk; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user
    ADD CONSTRAINT language_code_fk FOREIGN KEY (fk_language_code) REFERENCES public.language(code);


--
-- Name: language language_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language
    ADD CONSTRAINT language_fk_language_code FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: language_l language_l_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language_l
    ADD CONSTRAINT language_l_fk_language_code FOREIGN KEY (fk_language_code) REFERENCES public.language(code);


--
-- Name: language_t language_t_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language_t
    ADD CONSTRAINT language_t_fk_language_code FOREIGN KEY (fk_language_code) REFERENCES public.language(code);


--
-- Name: language_t language_t_fk_tag_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.language_t
    ADD CONSTRAINT language_t_fk_tag_uuid FOREIGN KEY (fk_tag_uuid) REFERENCES public.tag(uuid);


--
-- Name: security_action security_action_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_action
    ADD CONSTRAINT security_action_fk_language_code FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: security_action_l security_action_l_fk_security_action_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_action_l
    ADD CONSTRAINT security_action_l_fk_security_action_code FOREIGN KEY (fk_security_action_code) REFERENCES public.security_action(code);


--
-- Name: security_domain_security_action security_domain_action_fk_security_action_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_domain_security_action
    ADD CONSTRAINT security_domain_action_fk_security_action_code FOREIGN KEY (fk_security_action_code) REFERENCES public.security_action(code);


--
-- Name: security_domain_security_action security_domain_action_fk_security_domain_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_domain_security_action
    ADD CONSTRAINT security_domain_action_fk_security_domain_code FOREIGN KEY (fk_security_domain_code) REFERENCES public.security_domain(code);


--
-- Name: security_domain security_domain_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_domain
    ADD CONSTRAINT security_domain_fk_language_code FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: security_domain_l security_domain_l_fk_security_domain_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_domain_l
    ADD CONSTRAINT security_domain_l_fk_security_domain_code FOREIGN KEY (fk_security_domain_code) REFERENCES public.security_domain(code);


--
-- Name: security_permission security_permission_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission
    ADD CONSTRAINT security_permission_fk_language_code FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: security_permission security_permission_fk_security_action_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission
    ADD CONSTRAINT security_permission_fk_security_action_code FOREIGN KEY (fk_security_action_code) REFERENCES public.security_action(code);


--
-- Name: security_permission security_permission_fk_security_domain_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission
    ADD CONSTRAINT security_permission_fk_security_domain_code FOREIGN KEY (fk_security_domain_code) REFERENCES public.security_domain(code);


--
-- Name: security_permission_instances security_permission_instances_fk_security_permission_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission_instances
    ADD CONSTRAINT security_permission_instances_fk_security_permission_uuid FOREIGN KEY (fk_security_permission_uuid) REFERENCES public.security_permission(uuid);


--
-- Name: security_role security_role_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role
    ADD CONSTRAINT security_role_fk_language_code FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: security_role_l security_role_l_fk_security_role_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_l
    ADD CONSTRAINT security_role_l_fk_security_role_code FOREIGN KEY (fk_security_role_code) REFERENCES public.security_role(code);


--
-- Name: security_role_node security_role_node_parent_role_node_fk; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_node
    ADD CONSTRAINT security_role_node_parent_role_node_fk FOREIGN KEY (fk_role_node_uuid) REFERENCES public.security_role_node(uuid);


--
-- Name: security_role_node_permission security_role_node_permission_security_permission_fk; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_node_permission
    ADD CONSTRAINT security_role_node_permission_security_permission_fk FOREIGN KEY (fk_permission_uuid) REFERENCES public.security_permission(uuid);


--
-- Name: security_role_node_permission security_role_node_permission_security_role_node_fk; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_node_permission
    ADD CONSTRAINT security_role_node_permission_security_role_node_fk FOREIGN KEY (fk_role_node_uuid) REFERENCES public.security_role_node(uuid);


--
-- Name: security_role_security_permission security_role_permission_fk_security_permission_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_security_permission
    ADD CONSTRAINT security_role_permission_fk_security_permission_uuid FOREIGN KEY (fk_security_permission_uuid) REFERENCES public.security_permission(uuid);


--
-- Name: security_role_security_permission security_role_permission_fk_security_role_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_role_security_permission
    ADD CONSTRAINT security_role_permission_fk_security_role_code FOREIGN KEY (fk_security_role_code) REFERENCES public.security_role(code);


--
-- Name: security_user_a security_user_a_fk_security_user_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_a
    ADD CONSTRAINT security_user_a_fk_security_user_uuid FOREIGN KEY (fk_security_user_uuid) REFERENCES public.security_user(uuid);


--
-- Name: security_user_account security_user_account_user_uuid_fk; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_account
    ADD CONSTRAINT security_user_account_user_uuid_fk FOREIGN KEY (fk_security_user_uuid) REFERENCES public.security_user(uuid);


--
-- Name: security_user_role security_user_role_fk_role_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_role
    ADD CONSTRAINT security_user_role_fk_role_code FOREIGN KEY (fk_role_code) REFERENCES public.security_role(code);


--
-- Name: security_user_role security_user_role_fk_user_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_role
    ADD CONSTRAINT security_user_role_fk_user_uuid FOREIGN KEY (fk_user_uuid) REFERENCES public.security_user(uuid);


--
-- Name: security_user_t security_user_t_fk_security_user_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_t
    ADD CONSTRAINT security_user_t_fk_security_user_uuid FOREIGN KEY (fk_security_user_uuid) REFERENCES public.security_user(uuid);


--
-- Name: security_user_t security_user_t_fk_tag_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_user_t
    ADD CONSTRAINT security_user_t_fk_tag_uuid FOREIGN KEY (fk_tag_uuid) REFERENCES public.tag(uuid);


--
-- Name: security_permission_l ssecurity_permission_l_fk_security_permission_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.security_permission_l
    ADD CONSTRAINT ssecurity_permission_l_fk_security_permission_uuid FOREIGN KEY (fk_security_permission_uuid) REFERENCES public.security_permission(uuid);


--
-- Name: tag_a tag_a_fk_tag_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_a
    ADD CONSTRAINT tag_a_fk_tag_uuid FOREIGN KEY (fk_tag_uuid) REFERENCES public.tag(uuid);


--
-- Name: tag tag_fk_language_code; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag
    ADD CONSTRAINT tag_fk_language_code FOREIGN KEY (lang) REFERENCES public.language(code);


--
-- Name: tag_l tag_l_fk_tag_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_l
    ADD CONSTRAINT tag_l_fk_tag_uuid FOREIGN KEY (fk_tag_uuid) REFERENCES public.tag(uuid);


--
-- Name: tag_t tag_t_fk_next_tag_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_t
    ADD CONSTRAINT tag_t_fk_next_tag_uuid FOREIGN KEY (fk_next_tag_uuid) REFERENCES public.tag(uuid);


--
-- Name: tag_t tag_t_fk_tag_uuid; Type: FK CONSTRAINT; Schema: public; Owner: recyclone
--

ALTER TABLE ONLY public.tag_t
    ADD CONSTRAINT tag_t_fk_tag_uuid FOREIGN KEY (fk_tag_uuid) REFERENCES public.tag(uuid);


--
-- PostgreSQL database dump complete
--

