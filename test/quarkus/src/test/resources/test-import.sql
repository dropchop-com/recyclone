insert into language (code, title, lang, created, modified) values ('en', 'English', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sl', 'Slovene', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sl-nonstandard', 'Slovene (non standard)', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('hr', 'Croatian', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sr', 'Serbian', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sr-Cyrl', 'Serbian (Cyrillic)', 'en', now(), now());
insert into language (code, title, lang, created, modified) values ('sr-Cyrl-ME', 'Montenegro Serbian (Cyrillic)', 'en', now(), now());


insert into language_l (fk_language_code, title, lang, created, modified) values ('en', 'Angleščina', 'sl', now(), now());
insert into language_l (fk_language_code, title, lang, created, modified) values ('sl', 'Slovenščina', 'sl', now(), now());
