drop table if exists PLAYER_SESSION;
drop table if exists SESSION;
drop table if exists GAME_GENRE;
drop table if exists GAME;
drop table if exists PLAYER;
drop table if exists GENRE;

create table GENRE (
   name varchar(80) primary key
);

create table GAME (
  gid serial primary key,
  name varchar(80),
  developer varchar(80),
  unique (name, developer)
);

create table GAME_GENRE (
    gid int references GAME(gid),
    genre varchar(80) references GENRE(name)
);

create table PLAYER (
    pid serial primary key,
    name varchar(80),
    username varchar(40) unique not null,
    email varchar(40) check (position('@' in email) > 0) unique,
    password varchar(120) not null,
    token varchar(40)
);

create table SESSION (
     sid serial primary key,
     capacity int not null check (capacity > 0),
     gid int not null references GAME(gid),
     date DATE not null,
     owner int not null references PLAYER(pid)
);

create table PLAYER_SESSION (
    pid serial not null references PLAYER(pid),
    sid serial not null references SESSION(sid),
    primary key (pid, sid)
);

alter table player_session add constraint check_capacity check (check_capacity(sid));
