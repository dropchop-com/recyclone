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
SET session_replication_role = replica;

--
-- Data for Name: language; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.language (created, deactivated, modified, code, lang, title) FROM stdin;
2025-05-16 13:37:03.387069+00	\N	2025-05-16 13:37:03.387069+00	en	en	English
2025-05-16 13:37:03.390502+00	\N	2025-05-16 13:37:03.390502+00	sl	en	Slovene
2025-05-16 13:37:03.393629+00	\N	2025-05-16 13:37:03.393629+00	sl-nonstandard	en	Slovene (non standard)
2025-05-16 13:37:03.396752+00	\N	2025-05-16 13:37:03.396752+00	hr	en	Croatian
2025-05-16 13:37:03.399826+00	\N	2025-05-16 13:37:03.399826+00	sr	en	Serbian
2025-05-16 13:37:03.402975+00	\N	2025-05-16 13:37:03.402975+00	sr-Cyrl	en	Serbian (Cyrillic)
2025-05-16 13:37:03.406145+00	\N	2025-05-16 13:37:03.406145+00	sr-Cyrl-ME	en	Montenegro Serbian (Cyrillic)
\.


--
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.country (created, deactivated, modified, code, lang, title) FROM stdin;
2025-08-24 05:37:37.980427+00	\N	2025-08-24 05:37:37.980049+00	SI	en	Slovenia
2025-08-24 06:16:56.792084+00	\N	2025-08-24 06:16:56.792052+00	HR	en	Croatia
2025-08-24 06:16:56.792254+00	\N	2025-08-24 06:16:56.792245+00	RS	en	Serbia
\.


--
-- Data for Name: country_l; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.country_l (created, modified, title, fk_country_code, lang) FROM stdin;
2025-08-24 05:45:30.020986+00	2025-08-24 05:45:30.020986+00	Slovenija	SI	sl
2025-08-24 05:45:30.020989+00	2025-08-24 05:45:30.020989+00	Slovenija	SI	hr
2025-08-24 05:45:30.020992+00	2025-08-24 05:45:30.020993+00	Slovenija	SI	sr
2025-08-24 06:16:56.792188+00	2025-08-24 06:16:56.792189+00	Hrvaška	HR	sl
2025-08-24 06:16:56.792190+00	2025-08-24 06:16:56.792191+00	Hrvatska	HR	hr
2025-08-24 06:16:56.792192+00	2025-08-24 06:16:56.792194+00	Hrvatska	HR	sr
2025-08-24 06:16:56.792272+00	2025-08-24 06:16:56.792273+00	Srbija	RS	sl
2025-08-24 06:16:56.792274+00	2025-08-24 06:16:56.792275+00	Srbija	RS	hr
2025-08-24 06:16:56.792276+00	2025-08-24 06:16:56.792277+00	Srbija	RS	sr
\.


--
-- Data for Name: tag; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.tag (created, deactivated, modified, uuid, type, description, lang, name, title) FROM stdin;
2025-05-16 13:37:03.416705+00	2025-05-16 13:37:03.416705+00	2025-05-16 13:37:03.416705+00	c73847a8-836a-3ad3-b4f8-4a331248088d	LanguageGroup	\N	en	slavic	Slavic
2025-08-23 14:26:09.955550+00	\N	2025-08-23 14:26:09.955543+00	7d767bc7-86cc-36b7-a7a4-9bf79d100b3b	CountryGroup	\N	en	ex_yugoslavia	Ex Yugoslavia
2025-08-23 14:25:22.393886+00	\N	2025-08-23 14:25:22.393851+00	32656c8d-8361-3bc0-ad3b-442e665d4839	CountryGroup	\N	en	european_union	European Union
\.


--
-- Data for Name: country_t; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.country_t (idx, fk_tag_uuid, fk_country_code) FROM stdin;
0	32656c8d-8361-3bc0-ad3b-442e665d4839	SI
1	7d767bc7-86cc-36b7-a7a4-9bf79d100b3b	SI
0	32656c8d-8361-3bc0-ad3b-442e665d4839	HR
1	7d767bc7-86cc-36b7-a7a4-9bf79d100b3b	HR
0	7d767bc7-86cc-36b7-a7a4-9bf79d100b3b	RS
\.


--
-- Data for Name: dummy; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.dummy (created, deactivated, modified, code, description, lang, title) FROM stdin;
\.


--
-- Data for Name: dummy_l; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.dummy_l (created, modified, title, description, fk_dummy_code, lang) FROM stdin;
\.


--
-- Data for Name: language_l; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.language_l (created, modified, title, fk_language_code, lang) FROM stdin;
2025-05-16 13:37:03.40962+00	2025-05-16 13:37:03.40962+00	Angleščina	en	sl
2025-05-16 13:37:03.413271+00	2025-05-16 13:37:03.413271+00	Slovenščina	sl	sl
\.


--
-- Data for Name: language_t; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.language_t (idx, fk_tag_uuid, fk_language_code) FROM stdin;
0	c73847a8-836a-3ad3-b4f8-4a331248088d	sl
\.


--
-- Data for Name: security_action; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_action (created, deactivated, modified, code, description, lang, title) FROM stdin;
2025-05-16 13:37:03.430277+00	\N	2025-05-16 13:37:03.430277+00	*	\N	en	All
2025-05-16 13:37:03.433483+00	\N	2025-05-16 13:37:03.433483+00	view	\N	en	View
2025-05-16 13:37:03.436689+00	\N	2025-05-16 13:37:03.436689+00	create	\N	en	Create
2025-05-16 13:37:03.439954+00	\N	2025-05-16 13:37:03.439954+00	delete	\N	en	Delete
2025-05-16 13:37:03.44319+00	\N	2025-05-16 13:37:03.44319+00	update	\N	en	Update
\.


--
-- Data for Name: security_action_l; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_action_l (created, modified, title, description, fk_security_action_code, lang) FROM stdin;
2025-05-16 13:37:03.44635+00	2025-05-16 13:37:03.44635+00	Vse	\N	*	sl
2025-05-16 13:37:03.449836+00	2025-05-16 13:37:03.449836+00	Ogled	\N	view	sl
2025-05-16 13:37:03.453025+00	2025-05-16 13:37:03.453025+00	Ustvarjanje	\N	create	sl
2025-05-16 13:37:03.456177+00	2025-05-16 13:37:03.456177+00	Brisanje	\N	delete	sl
2025-05-16 13:37:03.459377+00	2025-05-16 13:37:03.459377+00	Spreminjanje	\N	update	sl
\.


--
-- Data for Name: security_domain; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_domain (created, deactivated, modified, code, description, lang, title) FROM stdin;
2025-05-16 13:37:03.463056+00	\N	2025-05-16 13:37:03.463056+00	*	\N	en	All
2025-05-16 13:37:03.466522+00	\N	2025-05-16 13:37:03.466522+00	localization.language	\N	en	Localization/Languages
2025-05-16 13:37:03.469815+00	\N	2025-05-16 13:37:03.469815+00	localization.country	\N	en	Localization/Countries
2025-05-16 13:37:03.472982+00	\N	2025-05-16 13:37:03.472982+00	security.action	\N	en	Security/Actions
2025-05-16 13:37:03.476142+00	\N	2025-05-16 13:37:03.476142+00	security.domain	\N	en	Security/SecurityDomains
2025-05-16 13:37:03.479225+00	\N	2025-05-16 13:37:03.479225+00	security.permission	\N	en	Security/Permissions
2025-05-16 13:37:03.482657+00	\N	2025-05-16 13:37:03.482657+00	security.role	\N	en	Security/Roles
2025-05-16 13:37:03.48587+00	\N	2025-05-16 13:37:03.48587+00	tagging.tag	\N	en	Tagging/Tags
\.


--
-- Data for Name: security_domain_l; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_domain_l (created, modified, title, description, fk_security_domain_code, lang) FROM stdin;
2025-05-16 13:37:03.489031+00	2025-05-16 13:37:03.489031+00	Vse	\N	*	sl
2025-05-16 13:37:03.492769+00	2025-05-16 13:37:03.492769+00	Lokalizacija/Jeziki	\N	localization.language	sl
2025-05-16 13:37:03.496109+00	2025-05-16 13:37:03.496109+00	Lokalizacija/Države	\N	localization.country	sl
2025-05-16 13:37:03.499446+00	2025-05-16 13:37:03.499446+00	Varnost/Akcije	\N	security.action	sl
2025-05-16 13:37:03.50283+00	2025-05-16 13:37:03.50283+00	Varnost/Področja	\N	security.domain	sl
2025-05-16 13:37:03.506161+00	2025-05-16 13:37:03.506161+00	Varnost/Dovoljenja	\N	security.permission	sl
2025-05-16 13:37:03.509613+00	2025-05-16 13:37:03.509613+00	Varnost/Vloge	\N	security.role	sl
2025-05-16 13:37:03.512902+00	2025-05-16 13:37:03.512902+00	Označevanje/Oznake	\N	tagging.tag	sl
\.


--
-- Data for Name: security_domain_security_action; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_domain_security_action (fk_security_action_code, fk_security_domain_code) FROM stdin;
*	*
view	*
create	*
delete	*
update	*
*	localization.language
*	localization.country
*	security.action
*	security.domain
*	security.permission
*	security.role
*	tagging.tag
view	localization.language
view	localization.country
view	security.action
view	security.domain
view	security.permission
view	security.role
view	tagging.tag
create	localization.language
create	localization.country
create	security.action
create	security.domain
create	security.permission
create	security.role
create	tagging.tag
delete	localization.language
delete	localization.country
delete	security.action
delete	security.domain
delete	security.permission
delete	security.role
delete	tagging.tag
update	localization.language
update	localization.country
update	security.action
update	security.domain
update	security.permission
update	security.role
update	tagging.tag
\.


--
-- Data for Name: security_permission; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_permission (created, deactivated, modified, uuid, description, fk_security_action_code, fk_security_domain_code, lang, title) FROM stdin;
2025-05-16 13:37:03.649091+00	\N	2025-05-16 13:37:03.649091+00	03965584-7609-44b4-8cf4-b54a3ac9e472	\N	*	*	\N	\N
2025-05-16 13:37:03.65258+00	\N	2025-05-16 13:37:03.65258+00	f321134e-d383-45c7-bbbf-befc44f41d0c	\N	*	localization.language	\N	\N
2025-05-16 13:37:03.655801+00	\N	2025-05-16 13:37:03.655801+00	0edf37b1-8b7c-4d81-9f17-05f683f54570	\N	view	localization.language	\N	\N
2025-05-16 13:37:03.658964+00	\N	2025-05-16 13:37:03.658964+00	9629c163-de68-4c6d-8bd5-9bdb5277bcbd	\N	create	localization.language	\N	\N
2025-05-16 13:37:03.662253+00	\N	2025-05-16 13:37:03.662253+00	83ea8f0e-5261-445d-beb5-c40f35d1fe4e	\N	update	localization.language	\N	\N
2025-05-16 13:37:03.666215+00	\N	2025-05-16 13:37:03.666215+00	0c4c4ed4-af82-4e3f-b244-f530e5f8b9b1	\N	delete	localization.language	\N	\N
2025-05-16 13:37:03.669404+00	\N	2025-05-16 13:37:03.669404+00	d4910ece-e4ad-4e6b-be92-c2e04f17670e	\N	*	localization.country	\N	\N
2025-05-16 13:37:03.672669+00	\N	2025-05-16 13:37:03.672669+00	91a9b501-1038-4d29-b35c-fa1109a7703a	\N	view	localization.country	\N	\N
2025-05-16 13:37:03.675981+00	\N	2025-05-16 13:37:03.675981+00	6b054a77-ed02-4921-ad9b-0bbda34c723a	\N	create	localization.country	\N	\N
2025-05-16 13:37:03.679256+00	\N	2025-05-16 13:37:03.679256+00	60aba466-f5bb-479d-8c6b-e33421a2837c	\N	update	localization.country	\N	\N
2025-05-16 13:37:03.682428+00	\N	2025-05-16 13:37:03.682428+00	aef17cd7-a042-4d5b-83ab-ffee045b3d5c	\N	delete	localization.country	\N	\N
2025-05-16 13:37:03.685829+00	\N	2025-05-16 13:37:03.685829+00	85c99e85-6528-4538-9907-c9613b0b7810	\N	*	security.action	\N	\N
2025-05-16 13:37:03.689397+00	\N	2025-05-16 13:37:03.689397+00	b0493f97-4041-4e7c-aa57-52a750e7910d	\N	view	security.action	\N	\N
2025-05-16 13:37:03.692828+00	\N	2025-05-16 13:37:03.692828+00	38710031-149a-4508-9764-1a9cb87f1062	\N	create	security.action	\N	\N
2025-05-16 13:37:03.696084+00	\N	2025-05-16 13:37:03.696084+00	8ba0a0a5-c43f-443d-a370-a86031c66611	\N	update	security.action	\N	\N
2025-05-16 13:37:03.699195+00	\N	2025-05-16 13:37:03.699195+00	19f3041d-b2ca-4344-8a68-6357e52fd8c4	\N	delete	security.action	\N	\N
2025-05-16 13:37:03.702227+00	\N	2025-05-16 13:37:03.702227+00	d2e85121-a3bc-4d5c-919e-8129bab592ac	\N	*	security.domain	\N	\N
2025-05-16 13:37:03.705585+00	\N	2025-05-16 13:37:03.705585+00	41b97271-7a51-4e75-a016-f27bb6f00dad	\N	view	security.domain	\N	\N
2025-05-16 13:37:03.708987+00	\N	2025-05-16 13:37:03.708987+00	941759d6-e22f-4c9c-b139-c854890ec84b	\N	create	security.domain	\N	\N
2025-05-16 13:37:03.712155+00	\N	2025-05-16 13:37:03.712155+00	a9cc303b-ca98-412a-8b1f-70a3db8fbd1d	\N	update	security.domain	\N	\N
2025-05-16 13:37:03.715153+00	\N	2025-05-16 13:37:03.715153+00	4c5f0bb5-31b6-4b25-9395-84985a8eb941	\N	delete	security.domain	\N	\N
2025-05-16 13:37:03.718152+00	\N	2025-05-16 13:37:03.718152+00	8b2cbcd9-4743-4d62-afb9-68fa9fd3fb29	\N	*	security.permission	\N	\N
2025-05-16 13:37:03.721186+00	\N	2025-05-16 13:37:03.721186+00	8c10fe8e-91c7-4423-b58f-4c30fc1295d8	\N	view	security.permission	\N	\N
2025-05-16 13:37:03.724137+00	\N	2025-05-16 13:37:03.724137+00	e693c294-f23d-459e-a4fc-5ff92f43daaf	\N	create	security.permission	\N	\N
2025-05-16 13:37:03.727125+00	\N	2025-05-16 13:37:03.727125+00	e1d441b4-d894-4291-96fd-ac5f5f2f262a	\N	update	security.permission	\N	\N
2025-05-16 13:37:03.730138+00	\N	2025-05-16 13:37:03.730138+00	be17be48-ad73-4a82-b9a7-9048469bbeee	\N	delete	security.permission	\N	\N
2025-05-16 13:37:03.733298+00	\N	2025-05-16 13:37:03.733298+00	f37bbdce-daf9-47f1-917f-532990c6c2a6	\N	*	security.role	\N	\N
2025-05-16 13:37:03.736711+00	\N	2025-05-16 13:37:03.736711+00	0ee92699-5ca2-42e0-ae62-201828fad6d2	\N	view	security.role	\N	\N
2025-05-16 13:37:03.739854+00	\N	2025-05-16 13:37:03.739854+00	6e62c68e-a24a-4a94-ba3c-1fb4cb267eec	\N	create	security.role	\N	\N
2025-05-16 13:37:03.742919+00	\N	2025-05-16 13:37:03.742919+00	9966baa8-d201-4b5c-9e5a-c9b9ab802073	\N	update	security.role	\N	\N
2025-05-16 13:37:03.745988+00	\N	2025-05-16 13:37:03.745988+00	c002b667-4a26-49c6-b67f-0de21e3dba7c	\N	delete	security.role	\N	\N
2025-05-16 13:37:03.749026+00	\N	2025-05-16 13:37:03.749026+00	fd0aad8c-a155-4589-af08-33b94b879860	\N	*	tagging.tag	\N	\N
2025-05-16 13:37:03.752096+00	\N	2025-05-16 13:37:03.752096+00	065e80ac-7f1e-41d2-8097-94783cf54717	\N	view	tagging.tag	\N	\N
2025-05-16 13:37:03.755193+00	\N	2025-05-16 13:37:03.755193+00	8d011b3c-e190-46f8-85aa-7b5658b2e895	\N	create	tagging.tag	\N	\N
2025-05-16 13:37:03.75853+00	\N	2025-05-16 13:37:03.75853+00	27892b9a-7af8-49e0-9777-12022e46f2eb	\N	update	tagging.tag	\N	\N
2025-05-16 13:37:03.761682+00	\N	2025-05-16 13:37:03.761682+00	19e5c479-e763-4dcf-92f0-c18a10ed1eac	\N	delete	tagging.tag	\N	\N
\.


--
-- Data for Name: security_permission_instances; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_permission_instances (fk_security_permission_uuid, instance) FROM stdin;
\.


--
-- Data for Name: security_permission_l; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_permission_l (created, modified, fk_security_permission_uuid, title, description, lang) FROM stdin;
\.


--
-- Data for Name: security_role; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_role (created, deactivated, modified, code, description, lang, title) FROM stdin;
2025-05-16 13:37:03.76501+00	\N	2025-05-16 13:37:03.76501+00	admin	\N	en	Administrator
2025-05-16 13:37:03.771553+00	\N	2025-05-16 13:37:03.771553+00	staff	\N	en	Staff
2025-05-16 13:37:03.777663+00	\N	2025-05-16 13:37:03.777663+00	user	\N	en	User
\.


--
-- Data for Name: security_role_l; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_role_l (created, modified, title, description, fk_security_role_code, lang) FROM stdin;
2025-05-16 13:37:03.768353+00	2025-05-16 13:37:03.768353+00	Skrbnik	\N	admin	sl
2025-05-16 13:37:03.774632+00	2025-05-16 13:37:03.774632+00	Uslužbenec	\N	staff	sl
2025-05-16 13:37:03.780803+00	2025-05-16 13:37:03.780803+00	Uporabnik	\N	user	sl
\.


--
-- Data for Name: security_role_node; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_role_node (max_parent_instance_level, fk_role_node_uuid, uuid, entity, entity_id, target, target_id) FROM stdin;
\.


--
-- Data for Name: security_role_node_permission; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_role_node_permission (allowed, fk_permission_uuid, fk_role_node_uuid, uuid, type, target, target_id) FROM stdin;
\.


--
-- Data for Name: security_role_security_permission; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_role_security_permission (fk_security_permission_uuid, fk_security_role_code) FROM stdin;
03965584-7609-44b4-8cf4-b54a3ac9e472	admin
f321134e-d383-45c7-bbbf-befc44f41d0c	staff
d4910ece-e4ad-4e6b-be92-c2e04f17670e	staff
b0493f97-4041-4e7c-aa57-52a750e7910d	staff
41b97271-7a51-4e75-a016-f27bb6f00dad	staff
8c10fe8e-91c7-4423-b58f-4c30fc1295d8	staff
0ee92699-5ca2-42e0-ae62-201828fad6d2	staff
fd0aad8c-a155-4589-af08-33b94b879860	staff
0edf37b1-8b7c-4d81-9f17-05f683f54570	user
91a9b501-1038-4d29-b35c-fa1109a7703a	user
065e80ac-7f1e-41d2-8097-94783cf54717	user
\.


--
-- Data for Name: security_user; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_user (is_female, created, deactivated, modified, uuid, default_email, default_phone, first_name, fk_country_code, fk_language_code, initials, last_name, maiden_name, middle_name, title) FROM stdin;
\.


--
-- Data for Name: security_user_a; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_user_a (fk_security_user_uuid, name, type, value) FROM stdin;
\.


--
-- Data for Name: security_user_account; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_user_account (created, deactivated, modified, fk_security_user_uuid, uuid, type, login_name, password, title, token) FROM stdin;
\.


--
-- Data for Name: security_user_role; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_user_role (fk_user_uuid, fk_role_code) FROM stdin;
\.


--
-- Data for Name: security_user_t; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.security_user_t (idx, fk_security_user_uuid, fk_tag_uuid) FROM stdin;
\.


--
-- Data for Name: tag_a; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.tag_a (fk_tag_uuid, name, type, value) FROM stdin;
\.


--
-- Data for Name: tag_l; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.tag_l (created, modified, fk_tag_uuid, title, description, lang) FROM stdin;
2025-05-16 13:37:03.420367+00	2025-05-16 13:37:03.420367+00	c73847a8-836a-3ad3-b4f8-4a331248088d	Slovanski Jezik	\N	sl
2025-05-16 13:37:03.423681+00	2025-05-16 13:37:03.423681+00	c73847a8-836a-3ad3-b4f8-4a331248088d	Slovanski Jezik	\N	hr
2025-05-16 13:37:03.420367+00	2025-05-16 13:37:03.420367+00	c73847a8-836a-3ad3-b4f8-4a331248088d	Slovanski Jezik	\N	sl
2025-05-16 13:37:03.423681+00	2025-05-16 13:37:03.423681+00	c73847a8-836a-3ad3-b4f8-4a331248088d	Slovanski Jezik	\N	hr
2025-05-16 13:37:03.423681+00	2025-05-16 13:37:03.423681+00	32656c8d-8361-3bc0-ad3b-442e665d4839	Evropska Unija	\N	hr
2025-05-16 13:37:03.423681+00	2025-05-16 13:37:03.423681+00	32656c8d-8361-3bc0-ad3b-442e665d4839	Evropska Unija	\N	sr
2025-05-16 13:37:03.423681+00	2025-05-16 13:37:03.423681+00	7d767bc7-86cc-36b7-a7a4-9bf79d100b3b	Ex Jugoslavija	\N	hr
2025-05-16 13:37:03.423681+00	2025-05-16 13:37:03.423681+00	7d767bc7-86cc-36b7-a7a4-9bf79d100b3b	Ex Jugoslavija	\N	sr
2025-05-16 13:37:03.423681+00	2025-05-16 13:37:03.423681+00	7d767bc7-86cc-36b7-a7a4-9bf79d100b3b	Ex Jugoslavija	\N	sl
2025-05-16 13:37:03.423681+00	2025-05-16 13:37:03.423681+00	32656c8d-8361-3bc0-ad3b-442e665d4839	Evropska Unija	\N	sl

\.


--
-- Data for Name: tag_t; Type: TABLE DATA; Schema: public; Owner: recyclone
--

COPY public.tag_t (idx, fk_next_tag_uuid, fk_tag_uuid) FROM stdin;
\.


--
-- PostgreSQL database dump complete
--
SET session_replication_role = DEFAULT;

