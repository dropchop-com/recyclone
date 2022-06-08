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
insert into security_domain(code, title, lang, created, modified) values ('localization.country', 'Localization/Countries', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('security.action', 'Security/Actions', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('security.domain', 'Security/Domains', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('security.permission', 'Security/Permissions', 'en', now(), now());
insert into security_domain(code, title, lang, created, modified) values ('security.role', 'Security/Roles', 'en', now(), now());

insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('*', 'Vse', 'sl', now(), now());
insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('localization.language', 'Lokalizacija/Jeziki', 'sl', now(), now());
insert into security_domain_l(fk_security_domain_code, title, lang, created, modified) values ('localization.country', 'Lokalizacija/Države', 'sl', now(), now());
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
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.country', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', '*');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.country', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', 'view');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.country', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', 'create');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.country', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', 'delete');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.language', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('localization.country', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.action', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.domain', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.permission', 'update');
insert into security_domain_security_action(fk_security_domain_code, fk_security_action_code) values ('security.role', 'update');

insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('03965584-7609-44b4-8cf4-b54a3ac9e472', '*', '*', now(), now());

insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('f321134e-d383-45c7-bbbf-befc44f41d0c', 'localization.language', '*', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('0edf37b1-8b7c-4d81-9f17-05f683f54570', 'localization.language', 'view', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('9629c163-de68-4c6d-8bd5-9bdb5277bcbd', 'localization.language', 'create', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('83ea8f0e-5261-445d-beb5-c40f35d1fe4e', 'localization.language', 'update', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('0c4c4ed4-af82-4e3f-b244-f530e5f8b9b1', 'localization.language', 'delete', now(), now());

insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('d4910ece-e4ad-4e6b-be92-c2e04f17670e', 'localization.country', '*', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('91a9b501-1038-4d29-b35c-fa1109a7703a', 'localization.country', 'view', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('6b054a77-ed02-4921-ad9b-0bbda34c723a', 'localization.country', 'create', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('60aba466-f5bb-479d-8c6b-e33421a2837c', 'localization.country', 'update', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('aef17cd7-a042-4d5b-83ab-ffee045b3d5c', 'localization.country', 'delete', now(), now());

insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('85c99e85-6528-4538-9907-c9613b0b7810', 'security.action', '*', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('b0493f97-4041-4e7c-aa57-52a750e7910d', 'security.action', 'view', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('38710031-149a-4508-9764-1a9cb87f1062', 'security.action', 'create', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('8ba0a0a5-c43f-443d-a370-a86031c66611', 'security.action', 'update', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('19f3041d-b2ca-4344-8a68-6357e52fd8c4', 'security.action', 'delete', now(), now());

insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('d2e85121-a3bc-4d5c-919e-8129bab592ac', 'security.domain', '*', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('41b97271-7a51-4e75-a016-f27bb6f00dad', 'security.domain', 'view', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('941759d6-e22f-4c9c-b139-c854890ec84b', 'security.domain', 'create', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('a9cc303b-ca98-412a-8b1f-70a3db8fbd1d', 'security.domain', 'update', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('4c5f0bb5-31b6-4b25-9395-84985a8eb941', 'security.domain', 'delete', now(), now());

insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('8b2cbcd9-4743-4d62-afb9-68fa9fd3fb29', 'security.permission', '*', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('8c10fe8e-91c7-4423-b58f-4c30fc1295d8', 'security.permission', 'view', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('e693c294-f23d-459e-a4fc-5ff92f43daaf', 'security.permission', 'create', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('e1d441b4-d894-4291-96fd-ac5f5f2f262a', 'security.permission', 'update', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('be17be48-ad73-4a82-b9a7-9048469bbeee', 'security.permission', 'delete', now(), now());

insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('f37bbdce-daf9-47f1-917f-532990c6c2a6', 'security.role', '*', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('0ee92699-5ca2-42e0-ae62-201828fad6d2', 'security.role', 'view', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('6e62c68e-a24a-4a94-ba3c-1fb4cb267eec', 'security.role', 'create', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('9966baa8-d201-4b5c-9e5a-c9b9ab802073', 'security.role', 'update', now(), now());
insert into security_permission(uuid, fk_security_domain_code, fk_security_action_code, created, modified)
values ('c002b667-4a26-49c6-b67f-0de21e3dba7c', 'security.role', 'delete', now(), now());

insert into security_role(code, title, lang, created, modified) values('admin', 'Administrator', 'en', now(), now());
insert into security_role_l(fk_security_role_code, title, lang, created, modified) values ('admin', 'Skrbnik', 'sl', now(), now());
insert into security_role(code, title, lang, created, modified) values('staff', 'Staff', 'en', now(), now());
insert into security_role_l(fk_security_role_code, title, lang, created, modified) values ('staff', 'Uslužbenec', 'sl', now(), now());
insert into security_role(code, title, lang, created, modified) values('user', 'User', 'en', now(), now());
insert into security_role_l(fk_security_role_code, title, lang, created, modified) values ('user', 'Uporabnik', 'sl', now(), now());

insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('admin', '03965584-7609-44b4-8cf4-b54a3ac9e472');

insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('staff', 'f321134e-d383-45c7-bbbf-befc44f41d0c');
insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('staff', 'd4910ece-e4ad-4e6b-be92-c2e04f17670e');
insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('staff', 'b0493f97-4041-4e7c-aa57-52a750e7910d');
insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('staff', '41b97271-7a51-4e75-a016-f27bb6f00dad');
insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('staff', '8c10fe8e-91c7-4423-b58f-4c30fc1295d8');
insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('staff', '0ee92699-5ca2-42e0-ae62-201828fad6d2');

insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('user', '0edf37b1-8b7c-4d81-9f17-05f683f54570');
insert into security_role_security_permission(fk_security_role_code, fk_security_permission_uuid)
values ('user', '91a9b501-1038-4d29-b35c-fa1109a7703a');
