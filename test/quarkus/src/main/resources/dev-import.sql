insert into language (code, title, lang, created, modified) values ('en', 'English', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sl', 'Slovene', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sl-nonstandard', 'Slovene (non standard)', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('hr', 'Croatian', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sr', 'Serbian', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sr-Cyrl', 'Serbian (Cyrillic)', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sr-Cyrl-ME', 'Montenegro Serbian (Cyrillic)', 'en', now(), now());


insert into language_l (fk_language_code, title, lang, created, modified) values ('en', 'Angleščina', 'sl', now(), now());
insert into language_l (fk_language_code, title, lang, created, modified) values ('sl', 'Slovenščina', 'sl', now(), now());

insert into security_action(code, title, lang, created, modified) values ('*', 'All', 'en', now(), now());
insert into security_action(code, title, lang, created, modified) values ('view', 'View', 'en', now(), now());
insert into security_action(code, title, lang, created, modified) values ('create', 'Create', 'en', now(), now());
insert into security_action(code, title, lang, created, modified) values ('delete', 'Delete', 'en', now(), now());
insert into security_action(code, title, lang, created, modified) values ('update', 'Update', 'en', now(), now());


insert into security_action_l (fk_security_action_code, title, lang, created, modified) values ('*', 'Vse', 'sl', now(), now());
insert into security_action_l (fk_security_action_code, title, lang, created, modified) values ('view', 'Ogled', 'sl', now(), now());
insert into security_action_l (fk_security_action_code, title, lang, created, modified) values ('create', 'Ustvarjanje', 'sl', now(), now());
insert into security_action_l (fk_security_action_code, title, lang, created, modified) values ('delete', 'Brisanje', 'sl', now(), now());
insert into security_action_l (fk_security_action_code, title, lang, created, modified) values ('update', 'Spreminjanje', 'sl', now(), now());


insert into security_domain(code, title, lang, created, modified) values ('*', 'All', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('localization.language', 'Localization/Languages', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('security.action', 'Security/Actions', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('security.domain', 'Security/Domains', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('security.permission', 'Security/Permissions', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('security.role', 'Security/Roles', 'en', now(), now());

insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('*', 'Vse', 'sl', now(), now());
insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('localization.language', 'Lokalizacija/Jeziki', 'sl', now(), now());
insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('security.action', 'Varnost/Akcije', 'sl', now(), now());
insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('security.domain', 'Varnost/Področja', 'sl', now(), now());
insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('security.permission', 'Varnost/Dovoljenja', 'sl', now(), now());
insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('security.role', 'Varnost/Vloge', 'sl', now(), now());

insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('*', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('*', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('*', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('*', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('*', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', 'update');
