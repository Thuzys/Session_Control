create table genres if not exists(
     name varchar(80) primary key
);

create table game if not exists(
    gid serial primary key,
    name varchar(80),
    developer varchar(80),
);

create table game_genres if not exists(
    gid int references game(gid),
    genre varchar(80) references genres(name)
);



