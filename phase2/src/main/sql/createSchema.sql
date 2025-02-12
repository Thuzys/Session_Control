drop table if exists PLAYER_SESSION;
drop table if exists SESSION;
drop table if exists GAME_GENRE;
drop table if exists GAME;
drop table if exists PLAYER;
drop table if exists GENRE;

create or replace function check_capacity(sid int)
    returns boolean as $$
declare
    capacityNumber int;
begin
    select capacity into capacityNumber from session where session.sid = check_capacity.sid;
    return (select count(*) from player_session where player_session.sid = check_capacity.sid) < capacityNumber;
end;
$$ language plpgsql;

create table GENRE (
   name varchar(80) primary key
);

create table GAME (
  gid serial primary key,
  name varchar(80),
  developer varchar(80)
);

create table GAME_GENRE (
    gid int references GAME(gid),
    genre varchar(80) references GENRE(name)
        CONSTRAINT valid_genre CHECK (genre IN ('RPG', 'Adventure', 'Shooter', 'Turn-based Strategy', 'Action', 'Platformer', 'Puzzle', 'Simulation', 'Sports', 'Racing', 'Fighting', 'Sandbox', 'Stealth', 'Survival', 'Horror', 'Open world', 'Tactical RPG', 'Real-time Strategy', 'MMO', 'Roguelike', 'MOBA', 'Tower Defense', 'Battle Royale'))
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
    pid serial not null references PLAYER(pid),
    sid serial not null references SESSION(sid),
    primary key (pid, sid)
);

alter table player_session add constraint check_capacity check (check_capacity(sid));
