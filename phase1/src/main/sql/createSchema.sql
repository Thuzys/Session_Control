drop table if exists PLAYER_SESSION;
drop table if exists SESSION;
drop table if exists GAME_GENRES;
drop table if exists GAME;
drop table if exists PLAYER;
drop table if exists GENRES;

create table GENRES (
    name varchar(80) primary key
);

create table GAME (
  gid serial primary key,
  name varchar(80),
  developer varchar(80)
);

create table GAME_GENRES (
    gid int references GAME(gid),
    genre varchar(80) references GENRES(name)
);

create table PLAYER (
    pid serial primary key,
    name varchar(80),
    email varchar(40) check (position('@' in email) > 0) unique,
    token varchar(40)
);

create table SESSION (
    sid serial primary key,
    capacity int not null check (capacity > 0),
    gid int not null references GAME(gid),
    date varchar(80) not null
);

create table PLAYER_SESSION (
    pid int not null references PLAYER(pid),
    sid int not null references SESSION(sid)
)
