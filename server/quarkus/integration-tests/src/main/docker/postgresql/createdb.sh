# a legacy file just for remembering
CREATE USER recyclone_test;
ALTER USER recyclone_test WITH PASSWORD 'recyclone_test';
CREATE DATABASE recyclone_test OWNER recyclone_test TEMPLATE 'template0' ENCODING 'UTF-8' LC_COLLATE 'sl_SI.UTF-8' LC_CTYPE 'sl_SI.UTF-8';
GRANT ALL PRIVILEGES ON DATABASE recyclone_test TO recyclone_test;

\connect recyclone_test postgres

CREATE EXTENSION "uuid-ossp";

#optional
CREATE EXTENSION "uuid_v1";