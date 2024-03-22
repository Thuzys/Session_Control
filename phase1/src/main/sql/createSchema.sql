drop table if exists students;
drop table if exists courses;
drop table if exists test;

create table courses (
  cid serial primary key,
  name varchar(80)
  genres integer[]
);

create table students (
  number int primary key,
  name varchar(80),
  course int references courses(cid)
);


create table test (
    name varchar(40)
)
