-- create database
create database javsterisk;

-- create user for database
create user 'javsterisk'@'localhost' identified by 'javsterisk';

-- grant privileges for user on database
grant all privileges on javsterisk.* to 'javsterisk'@'localhost';