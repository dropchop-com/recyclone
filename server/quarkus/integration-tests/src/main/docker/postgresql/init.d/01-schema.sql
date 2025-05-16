create table language
(
    code varchar(50) not null
        constraint language_pkey
            primary key,
    title varchar(255),
    fk_language_code varchar(50)
        constraint language_lang_language_code_fk
        references language (code),
    created timestamp with time zone,
    modified timestamp with time zone,
    deactivated timestamp with time zone
);

insert into language (code, title, fk_language_code, created, modified) values ('en', 'English', 'en', now(), now());
insert into language (code, title, fk_language_code, created, modified) values ('sl', 'English', 'en', now(), now());

