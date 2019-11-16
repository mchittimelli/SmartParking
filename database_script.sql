DROP SCHEMA IF EXISTS parking;
CREATE SCHEMA parking COLLATE = utf8_general_ci;

USE parking;

drop table if exists slots;
CREATE TABLE slots (
	slot_id varchar(10) NOT NULL,
	availability boolean,
	PRIMARY KEY (slot_id)
	);


insert into slots
select 'A1', 0
union
select 'A2', 0
union
select 'A3', 0
union
select 'A4', 0
union
select 'A5', 0
union
select 'A6', 0
union
select 'A7', 0
union
select 'A8', 0
union
select 'B1', 0
union
select 'B2', 0
union
select 'B3', 0
union
select 'B4', 0
union
select 'B5', 0
union
select 'B6', 0
union
select 'B7', 0
union
select 'B8', 0;